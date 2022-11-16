package com.nurtivillage.java.geonixApplication.service;
import com.nurtivillage.java.geonixApplication.RazorPayClientConfig;
import com.nurtivillage.java.geonixApplication.controller.OrderController;
import com.nurtivillage.java.geonixApplication.dao.OrderRepository;
import com.nurtivillage.java.geonixApplication.dao.PaymentRepository;
import com.nurtivillage.java.geonixApplication.model.Payment;
import com.nurtivillage.java.geonixApplication.model.UserOrder;
import com.nurtivillage.java.geonixApplication.model.Signature;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Refund;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnlinePaymentService {
	private final static Logger log= LogManager.getLogger(OrderController.class);
	@Autowired
	public OrderService orderService;
	private RazorpayClient razorPayClient;
	public OnlinePaymentService(RazorPayClientConfig razorpayClientConfig)throws RazorpayException{
		this.razorPayClient=new RazorpayClient(razorpayClientConfig.getKey(),razorpayClientConfig.getSecret());
	}
	@Autowired
	OrderRepository orderRepository;
    public Order createOrderOnRazorpay(UserOrder order,RazorpayClient client) throws Exception{
        
        String razorpayOrderId = null;
        try {
           String amountInPaise=convertRupeeToPaise(String.valueOf(order.getAmount()));
            JSONObject options = new JSONObject();
            options.put("amount", amountInPaise);
            options.put("currency", "INR");
            options.put("receipt","order_"+order.getId());
            Order orderRes = client.Orders.create(options);
            return orderRes;
        } catch (RazorpayException e) {
            throw e;
        }
    }
	@Autowired
	PaymentRepository paymentRepo;
	@Transactional
	 public Payment savePayment(String razorpayOrderId,UserOrder userOrder) throws Exception {
	  try {  Payment p=new Payment();
	    p.setRazorpayOrderId(razorpayOrderId);
	    p.setOrder(userOrder);
	    Payment returnPayment =  paymentRepo.save(p);
		  log.info("Sending Mail To Admin for order received --Start");
		  orderService.sendMailToAdminForOrder(userOrder);
//               mailSender.send(mail);
		  log.info("Sending Mail To Admin for order received --End");

		  log.info("Sending Mail To buyer for order received --Start");
		  orderService.sendMailToBuyerForOrder(userOrder);
		  return returnPayment;
	  }
	  catch(Exception e) {
		  throw e;
	  }
	}
	@Transactional
	public String validateAndUpdateOrder(final String razorpayOrderId,final String razorpayPaymentId,final String razorSignature,final String secret) {
		String error=null;
		try {
			Payment payment=paymentRepo.findByRazorpayOrderId(razorpayOrderId);
			
			
	        String generatedSignature = Signature.calculateRFC2104HMAC(payment.getRazopayOrderId() + "|" + razorpayPaymentId, secret);
	      
	    
	        if(generatedSignature.equals(razorSignature)) {
	        	payment.setRazorpayOrderId(razorpayOrderId);
	        	payment.setRazorpaySignature(razorSignature);
	        	payment.setRazorpayPaymentId(razorpayPaymentId);
	        	UserOrder userOrder=payment.getOrder();
	        	userOrder.setPaymentStatus("PAID");
	        	orderRepository.save(userOrder);
	        	
	        	paymentRepo.save(payment);
				log.info("Sending Mail To Admin for order received --Start");
				orderService.sendMailToAdminForOrder(userOrder);
//               mailSender.send(mail);
				log.info("Sending Mail To Admin for order received --End");

				log.info("Sending Mail To buyer for order received --Start");
				orderService.sendMailToBuyerForOrder(userOrder);
	        }
	        else {
	        	error="Payment validation failed..Signature Doesn't match.";
	        }
			
		}catch(Exception e) {
			error="Payment validation failed";
		}
		return error;
	}
	public String convertRupeeToPaise(String amount) {
		BigDecimal b=new BigDecimal(amount);
		BigDecimal value=b.multiply(new BigDecimal("100"));
		return value.setScale(0, RoundingMode.UP).toString();
		
	}
	public void refund(Long id) throws Exception {
		try {
			Payment paymentInfo = paymentRepo.findByOrderId(id);
			if(paymentInfo == null){
				new Exception("order payment record not found");
			}
			JSONObject refundRequest = new JSONObject();
			refundRequest.put("payment_id",paymentInfo.getRazorpayPaymentId());
			Refund refund = this.razorPayClient.Payments.refund(refundRequest);
		} catch (Exception e) {
			throw e;
		}
	}
}
