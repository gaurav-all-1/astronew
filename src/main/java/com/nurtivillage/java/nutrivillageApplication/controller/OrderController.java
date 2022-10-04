package com.nurtivillage.java.nutrivillageApplication.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.websocket.server.PathParam;

import com.nurtivillage.java.nutrivillageApplication.RazorPayClientConfig;
import com.nurtivillage.java.nutrivillageApplication.Request.OrderRequest;
import com.nurtivillage.java.nutrivillageApplication.dao.UserRepository;
import com.nurtivillage.java.nutrivillageApplication.dto.StatusRequest;
import com.nurtivillage.java.nutrivillageApplication.model.OrderDetails;
import com.nurtivillage.java.nutrivillageApplication.model.Payment;
import com.nurtivillage.java.nutrivillageApplication.model.Status;
import com.nurtivillage.java.nutrivillageApplication.model.User;
import com.nurtivillage.java.nutrivillageApplication.model.UserOrder;
import com.nurtivillage.java.nutrivillageApplication.service.ApiResponseService;
import com.nurtivillage.java.nutrivillageApplication.service.LoggedInUserService;
import com.nurtivillage.java.nutrivillageApplication.service.OnlinePaymentService;
import com.nurtivillage.java.nutrivillageApplication.service.OrderService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Transient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(path = "/order")
@Transactional
public class OrderController {
	private final static Logger log=LogManager.getLogger(OrderController.class);
        @Autowired
        public OrderService orderService;
        @Autowired
        public LoggedInUserService userService;
        @Autowired 
        public OnlinePaymentService onlinePaymentService;
        private RazorpayClient razorpayClient;
        private RazorPayClientConfig razorpayClientConfig;
        
        @Autowired
        private JavaMailSender mailSender;
        @Autowired
        private UserRepository userRepo;
        
        @Autowired
        public OrderController(RazorPayClientConfig razorpayClientConfig) throws RazorpayException{
        	this.razorpayClientConfig=razorpayClientConfig;
        	this.razorpayClient=new RazorpayClient(razorpayClientConfig.getKey(),razorpayClientConfig.getSecret());
        }
        
        
        @GetMapping("/list")
        public ResponseEntity<ApiResponseService> allOrder(){
            try{
                List<UserOrder> orderList = orderService.getAllOrder();
                ApiResponseService res = new ApiResponseService("order List",true,orderList);
                return  new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
            }catch(Exception e){
                System.out.println(e);
                ApiResponseService res = new ApiResponseService(e.getMessage(),false,Arrays.asList("error"));
                return new ResponseEntity<ApiResponseService>(res,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @GetMapping("/cancel")
        public ResponseEntity<ApiResponseService> cancelOrder(){
            try{
                List<UserOrder> orderList = orderService.getAllCancelOrder();
                ApiResponseService res = new ApiResponseService("order List",true,orderList);
                return  new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
            }catch(Exception e){
                System.out.println(e);
                ApiResponseService res = new ApiResponseService(e.getMessage(),false,Arrays.asList("error"));
                return new ResponseEntity<ApiResponseService>(res,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @PostMapping("/create")
        public ResponseEntity<ApiResponseService> createOrder(@RequestBody OrderRequest orderRequest){
            try{
//                Long orderNO = orderService.getLastOrderNO();
                double amount = orderRequest.getAmount();
                User user = userService.userDetails();
                UserOrder order = new UserOrder(amount,user,orderRequest.getCartItem().size(),Status.ordered,orderRequest.getShippingAddress(),orderRequest.getPaymentMethod());
                //varify amount
//                boolean checker = orderService.amountVarify(amount,orderRequest.getCartItem());
//                if(!checker){
//                    throw new Exception("amount not varify");
//                }
                UserOrder orderCreate = orderService.createOrder(order);
                orderService.createOrderDetails(orderRequest.getCartItem(),orderCreate);

//                mailSender.send(mailBuyer);
                log.info("Sending Mail To buyer for order received --End");
                if(!orderRequest.getPaymentMethod().equals("COD")){
                    Order orderRes = onlinePaymentService.createOrderOnRazorpay(orderCreate,this.razorpayClient);
                    onlinePaymentService.savePayment(orderRes.get("id"), orderCreate);
                    ApiResponseService res = new ApiResponseService("make payment",true,Arrays.asList(orderRes.get("id"),orderRes.get("amount")));
                    return  new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
                }

                ApiResponseService res = new ApiResponseService("order placed",true,null);
                return  new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
            }catch(Exception e){
                System.out.println(e);
                ApiResponseService res = new ApiResponseService(e.getMessage(),false,Arrays.asList(e));
                return new ResponseEntity<ApiResponseService>(res,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } 
        @PostMapping("/product")
        public ResponseEntity<ApiResponseService> createSingleProductOrder(@RequestBody OrderRequest orderRequest){
            try{
//                Long orderNO = orderService.getLastOrderNO();
                double amount = orderRequest.getAmount();
                User user = userService.userDetails();
//               boolean inStock=orderService.checkQuantity(orderRequest.getProductId(), orderRequest.getVariantId(), orderRequest.getQuantity());
//               if(!inStock) {
//            	   throw new Exception("Not in Stock");
//               }
               UserOrder order = new UserOrder(amount,user,1,Status.ordered,orderRequest.getShippingAddress(),orderRequest.getPaymentMethod());
                //verify amount
//                boolean checker = orderService.checkAmount(orderRequest);
//                if(!checker){
//                    throw new Exception("Incorrect amount");
//                }
                UserOrder orderCreate = orderService.createOrder(order);
//               OrderDetails data =
                       orderService.createSingleOrderDetails(orderRequest.getProductId(),orderRequest.getVariantId(),orderRequest.getQuantity(),orderCreate);

//               mailSender.send(mailBuyer);
               log.info("Sending Mail To buyer for order received --End");
               
                if(!orderRequest.getPaymentMethod().equals("COD")){
                    Order orderRes = onlinePaymentService.createOrderOnRazorpay(orderCreate,this.razorpayClient);
                    onlinePaymentService.savePayment(orderRes.get("id"), orderCreate);
                    ApiResponseService res = new ApiResponseService("make payment",true,Arrays.asList(orderRes.get("id"),orderRes.get("amount")));
                    return  new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
                }
                ApiResponseService res = new ApiResponseService("order placed",true,null);
                return  new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
            }catch(Exception e){
                System.out.println(e);
                ApiResponseService res = new ApiResponseService(e.getMessage(),false,Arrays.asList(e));
                return new ResponseEntity<ApiResponseService>(res,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } 
        
        @PutMapping("/validatePayment")
        public ResponseEntity<ApiResponseService> updateOrder(@RequestBody Payment payment){
        	try {
        		String error=onlinePaymentService.validateAndUpdateOrder(payment.getRazopayOrderId(),payment.getRazorpayPaymentId(),payment.getRazorpaySignature(),razorpayClientConfig.getSecret());
        	     if(error!=null) {
                    ApiResponseService res = new ApiResponseService("something went wrong",false,Arrays.asList("error"));
        	    	return new ResponseEntity<ApiResponseService>(res,HttpStatus.BAD_REQUEST);
        	     }
                 ApiResponseService res = new ApiResponseService("ok",true,Arrays.asList());
        	     return new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
        	}
        	catch(Exception e) {
                ApiResponseService res = new ApiResponseService(e.getMessage(),false,Arrays.asList("error"));
        		return new ResponseEntity<ApiResponseService>(res,HttpStatus.INTERNAL_SERVER_ERROR);	
        	}
			
        	
        }
        
        // Guest Order- without login create order
        @PostMapping("/guest")
        public ResponseEntity<ApiResponseService> createGuestOrder(@RequestBody OrderRequest orderRequest){
            try{
//                Long orderNO = orderService.getLastOrderNO();
                double amount = orderRequest.getAmount();
                User user=userRepo.findByEmail(orderRequest.getShippingAddress().getEmail());
                  if(user==null) {
                user = orderService.createGuestUser(orderRequest.getShippingAddress());}
//               boolean inStock=orderService.checkQuantity(orderRequest.getProductId(), orderRequest.getVariantId(), orderRequest.getQuantity());
//               if(!inStock) {
//            	   throw new Exception("Not in Stock");
//               }
               UserOrder order = new UserOrder(amount,user,1,Status.ordered,orderRequest.getShippingAddress(),orderRequest.getPaymentMethod());
                //verify amount
//                boolean checker = orderService.checkAmount(orderRequest);
//                if(!checker){
//                    throw new Exception("Incorrect amount");
//                }
                UserOrder orderCreate = orderService.createOrder(order);
//               OrderDetails data =
                       orderService.createSingleOrderDetails(orderRequest.getProductId(),orderRequest.getVariantId(),orderRequest.getQuantity(),orderCreate);
               log.info("Sending Mail To Admin for order received --Start");
               orderService.sendMailToAdminForOrder(orderCreate);
//               mailSender.send(mail);
               log.info("Sending Mail To Admin for order received --End");
               
               log.info("Sending Mail To buyer for order received --Start");
               orderService.sendMailToBuyerForOrder(orderCreate);
//               mailSender.send(mailBuyer);
               log.info("Sending Mail To buyer for order received --End");
               
               Map<String,String> guestInfo=orderService.guestInfo(user);
                if(!orderRequest.getPaymentMethod().equals("COD")){
                    Order orderRes = onlinePaymentService.createOrderOnRazorpay(orderCreate,this.razorpayClient);
                    onlinePaymentService.savePayment(orderRes.get("id"), orderCreate);
                    ApiResponseService res = new ApiResponseService("make payment",true,Arrays.asList(orderRes.get("id"),orderRes.get("amount")),guestInfo);
                    return  new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
                }
//                System.out.print(data);
                ApiResponseService res = new ApiResponseService("order placed",true,null,guestInfo);
                return  new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
            }catch(Exception e){
                System.out.println(e);
                ApiResponseService res = new ApiResponseService(e.getMessage(),false,Arrays.asList(e));
                return new ResponseEntity<ApiResponseService>(res,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } 

        @GetMapping("/detail/{id}")
        public ResponseEntity<ApiResponseService> orderDetail(@PathVariable Long id){
            try{
                UserOrder order = orderService.getOrder(id);
                List<OrderDetails> orderDetails = orderService.findByUesrOrder(order);
                
                ApiResponseService res = new ApiResponseService("order detail",true,orderDetails);
                return  new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
            }catch(Exception e){
                System.out.println(e);
                ApiResponseService res = new ApiResponseService(e.getMessage(),false,Arrays.asList("error"));
                return new ResponseEntity<ApiResponseService>(res,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @PutMapping("status")
        public ResponseEntity<ApiResponseService> orderStatus(@RequestBody StatusRequest statusRequest){
            try{
                UserOrder orderCreate = orderService.orderStatus(statusRequest);
                ApiResponseService res = new ApiResponseService("orderStatus",true,Arrays.asList(orderCreate));
                return  new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
            }catch(Exception e){
                System.out.println(e);
                ApiResponseService res = new ApiResponseService(e.getMessage(),false,Arrays.asList("error"));
                return new ResponseEntity<ApiResponseService>(res,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @GetMapping("/user/list")
        public ResponseEntity<ApiResponseService> userOrderList(){
            try{
                User user = userService.userDetails();
                List<?> order = orderService.getUserOrder(user);
                
                ApiResponseService res = new ApiResponseService("User order list",true,order);
                return  new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
            }catch(Exception e){
                System.out.println(e);
                ApiResponseService res = new ApiResponseService(e.getMessage(),false,Arrays.asList("error"));
                return new ResponseEntity<ApiResponseService>(res,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @GetMapping(value="/usercancelorderList")
        public ResponseEntity<ApiResponseService> getUserCancelOrder(){
            try {
                User user = userService.userDetails();
                List<UserOrder> orderList = orderService.getUserCancelOrder(user.getId());
                ApiResponseService res = new ApiResponseService("order List",true,orderList);
                return  new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
            } catch (Exception e) {
                System.out.println(e);
                ApiResponseService res = new ApiResponseService(e.getMessage(),false,Arrays.asList("error"));
                return new ResponseEntity<ApiResponseService>(res,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        @PutMapping("/userOrderCancel")
        public ResponseEntity<ApiResponseService> userOrderCancel(@RequestBody StatusRequest statusRequest){
            try{
                statusRequest.setStatus("canceled");
                UserOrder orderCreate = orderService.orderStatus(statusRequest);
                ApiResponseService res = new ApiResponseService("orderStatus",true,Arrays.asList(orderCreate));
                return  new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
            }catch(Exception e){
                System.out.println(e);
                ApiResponseService res = new ApiResponseService(e.getMessage(),false,Arrays.asList("error"));
                return new ResponseEntity<ApiResponseService>(res,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } 

        // @GetMapping(value="/refund/{order_id}")
        // public ResponseEntity<ApiResponseService> getMethodName(@PathVariable Long order_id) {
        //     try {
        //         Optional<UserOrder> order = orderService.getOrder(order_id);
        //         if(order.isEmpty()){
        //             new Exception("order not found");
        //         }
        //         onlinePaymentService.refund(order_id);
        //         // UserOrder orderCreate = orderService.orderStatus(statusRequest);
        //         ApiResponseService res = new ApiResponseService("orderStatus",true,Arrays.asList(orderCreate));
        //         return  new ResponseEntity<ApiResponseService>(res,HttpStatus.OK);
        //     } catch (Exception e) {
        //         throw e;
        //     }
        // }

        @GetMapping("/getOrderDetails/{orderDetailId}")
        public ResponseEntity<?> getOrderDetails(@PathVariable Long orderDetailId){
        	try {log.info("Fetching order details with id: "+orderDetailId+"--Start");
        		OrderDetails orderDetails=orderService.getOrderDetail(orderDetailId);
        		log.info("Fetching order details with id: "+orderDetailId+"--End");
        		return new ResponseEntity<OrderDetails>(orderDetails,HttpStatus.OK);
        	}
        	catch(Exception e) {
        		return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        	}
        }
        
        @GetMapping("/{orderId}")
        public ResponseEntity<?> getUserOrder(@PathVariable Long orderId){
        	try {log.info("Fetching user order with id: "+orderId+"--Start");
        		UserOrder order=orderService.getOrder(orderId);
        		log.info("Fetching user order with id: "+orderId+"--End");
        		return new ResponseEntity<UserOrder>(order,HttpStatus.OK);
        	}
        	catch(Exception e) {
        		return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        	}
        }

}
