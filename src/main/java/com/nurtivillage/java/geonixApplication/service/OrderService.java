package com.nurtivillage.java.geonixApplication.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import com.itextpdf.html2pdf.HtmlConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nurtivillage.java.geonixApplication.Request.OrderRequest;
import com.nurtivillage.java.geonixApplication.dao.OrderDetailsRepository;
import com.nurtivillage.java.geonixApplication.dao.OrderRepository;
import com.nurtivillage.java.geonixApplication.dao.UserProfileRepository;
import com.nurtivillage.java.geonixApplication.dao.UserRepository;
import com.nurtivillage.java.geonixApplication.dto.StatusRequest;
import com.nurtivillage.java.geonixApplication.jwt.JwtTokenUtil;
import com.nurtivillage.java.geonixApplication.model.Cart;
import com.nurtivillage.java.geonixApplication.model.Inventory;
import com.nurtivillage.java.geonixApplication.model.Offer;
import com.nurtivillage.java.geonixApplication.model.OrderDetails;
import com.nurtivillage.java.geonixApplication.model.ShippingAddress;
import com.nurtivillage.java.geonixApplication.model.Status;
import com.nurtivillage.java.geonixApplication.model.User;
import com.nurtivillage.java.geonixApplication.model.UserOrder;
import com.nurtivillage.java.geonixApplication.model.UserProfile;
import com.nurtivillage.java.geonixApplication.model.Variant;

@Service
@Transactional
public class OrderService {
	private static final Logger log = LogManager.getLogger(OrderService.class);
	@Autowired
	public OrderRepository orderRepository;

	@Autowired
	public OrderDetailsRepository orderDetailsRepository;

//	@Autowired
//	public OrderService orderService;

	@Autowired
	private AWSS3Service awss3Service;
	@Autowired
	public CartService cartService;

	@Autowired
	public InventoryService inventoryService;

	@Autowired
	private UserProfileRepository profileRepo;

	@Autowired
	public OfferService offerService;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Value("${spring.mail.username}")
	private String fromMail;

	double totalAmount = 0;

	public List<UserOrder> getAllOrder() {
		List<UserOrder> userOrder = orderRepository.findByStatusNotOrderByCreatedAtAsc(Status.canceled);
		return userOrder;
	}

	public UserOrder createOrder(UserOrder order) {
		UserOrder orderCreate = orderRepository.save(order);
		return orderCreate;
	}

//	@Async
	public void createOrderDetails(List<Cart> cartItems, UserOrder order) {// (List<Product>
																							// product,UserOrder
																							// order,List<Long>
																							// quantity){
		List<OrderDetails> orderAllItem = new ArrayList<>();
		cartItems.forEach((var) -> {
			Cart cartItem = cartService.cartItemById(var.getId());
			Variant variant = cartItem.getInventory().getVariant();
			int price = cartItem.getInventory().getPrice();
//			List<Offer> offer = offerService.getOffersByProduct(cartItem.getProduct().getId());
//			if (offer.size() != 0) {
//				OrderDetails orderItem = new OrderDetails(cartItem.getProduct(), order, cartItem.getQuantity(), variant,
//						offer.get(0), price);
//				orderAllItem.add(orderItem);
//			} else {
				OrderDetails orderItem = new OrderDetails(cartItem.getProduct(), order, cartItem.getQuantity(), variant,
						null, price);
			orderDetailsRepository.save(orderItem);
//			}
		});
		try {
			log.info("Sending Mail To Admin for order received --Start");
			sendMailToAdminForOrder(order);
//                mailSender.send(mailAdmin);
			log.info("Sending Mail To Admin for order received --End");

			log.info("Sending Mail To buyer for order received --Start");
			sendMailToBuyerForOrder(order);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		cartService.cartClear();
	}

	public void createOrderDetailsForRZP(List<Cart> cartItems, UserOrder order) {// (List<Product>
		// product,UserOrder
		// order,List<Long>
		// quantity){
		List<OrderDetails> orderAllItem = new ArrayList<>();
		cartItems.forEach((var) -> {
			Cart cartItem = cartService.cartItemById(var.getId());
			Variant variant = cartItem.getInventory().getVariant();
			int price = cartItem.getInventory().getPrice();
//			List<Offer> offer = offerService.getOffersByProduct(cartItem.getProduct().getId());
//			if (offer.size() != 0) {
//				OrderDetails orderItem = new OrderDetails(cartItem.getProduct(), order, cartItem.getQuantity(), variant,
//						offer.get(0), price);
//				orderAllItem.add(orderItem);
//			} else {
			OrderDetails orderItem = new OrderDetails(cartItem.getProduct(), order, cartItem.getQuantity(), variant,
					null, price);
			orderDetailsRepository.save(orderItem);
//			}
		});
		cartService.cartClear();
	}

	public void createSingleOrderDetails(Long productId, int variantId, int quantity, UserOrder order) {// (List<Product>
																												// product,UserOrder
																												// order,List<Long>
																												// quantity){
		OrderDetails orderItem = null;
		Inventory inventory = inventoryService.getProductVariantInventory(productId, variantId);
//		List<Offer> offer = offerService.getOffersByProduct(inventory.getProduct().getId());
//		if (offer.size() != 0) {
//			orderItem = new OrderDetails(inventory.getProduct(), order, quantity, inventory.getVariant(), offer.get(0),
//					inventory.getPrice());
//
//		} else {
			orderItem = new OrderDetails(inventory.getProduct(), order, quantity, inventory.getVariant(), null,
					inventory.getPrice());

//		}
		orderDetailsRepository.save(orderItem);
		log.info("Sending Mail To Admin for order received --Start");
		try {
			sendMailToAdminForOrder(order);
//               mailSender.send(mail);
			log.info("Sending Mail To Admin for order received --End");

			log.info("Sending Mail To buyer for order received --Start");
			sendMailToBuyerForOrder(order);
		}catch (Exception e){
			e.printStackTrace();
		}

//		return orderItem;
	}

	public void createSingleOrderDetailsForRzp(Long productId, int variantId, int quantity, UserOrder order) {// (List<Product>
		// product,UserOrder
		// order,List<Long>
		// quantity){
		OrderDetails orderItem = null;
		Inventory inventory = inventoryService.getProductVariantInventory(productId, variantId);
//		List<Offer> offer = offerService.getOffersByProduct(inventory.getProduct().getId());
//		if (offer.size() != 0) {
//			orderItem = new OrderDetails(inventory.getProduct(), order, quantity, inventory.getVariant(), offer.get(0),
//					inventory.getPrice());
//
//		} else {
		orderItem = new OrderDetails(inventory.getProduct(), order, quantity, inventory.getVariant(), null,
				inventory.getPrice());

//		}
		orderDetailsRepository.save(orderItem);

//		return orderItem;
	}

	public void sendEmailsforOrders(UserOrder order){
				log.info("Sending Mail To Admin for order received --Start");
				try {
					sendMailToAdminForOrder(order);
//               mailSender.send(mail);
					log.info("Sending Mail To Admin for order received --End");

					log.info("Sending Mail To buyer for order received --Start");
					sendMailToBuyerForOrder(order);
				}catch (Exception e)
				{
					e.printStackTrace();
				}
	}

	public OrderDetails getOrderDetail(Long id) throws Exception {
		try {
			Optional<OrderDetails> orderDetails = orderDetailsRepository.findById(id);
			if (orderDetails.isPresent()) {
				return orderDetails.get();
			}
			log.error("Order Details are not present with this id :" + id);
			throw new Exception("Order Details are not present with this id :" + id);
		} catch (Exception e) {
			throw e;
		}
	}

	public List<OrderDetails> findByUesrOrder(UserOrder order) {
		List<OrderDetails> orderDetails = orderDetailsRepository.findByUesrOrder(order);
		return orderDetails;
	}

	public UserOrder getOrder(Long id) throws Exception {
		try {
			Optional<UserOrder> order = orderRepository.findById(id);
			if (order.isPresent()) {
				return order.get();
			}
			throw new Exception("User order not present with this id: " + id);
		} catch (Exception e) {
			throw e;
		}
	}

	public UserOrder orderStatus(StatusRequest statusRequest) throws Exception {
		try {
			UserOrder orderInfo = getOrder(statusRequest.getId());

			Status updateStatus = getStatus(statusRequest.getStatus());
			orderInfo.setStatus(updateStatus);
			orderInfo.setComment(statusRequest.getComment());
			UserOrder updatedOrder = orderRepository.save(orderInfo);
			return updatedOrder;
		} catch (Exception e) {
			throw e;
		}

	}


	public UserOrder trackingStatus(StatusRequest statusRequest) throws Exception {
		try {
			UserOrder orderInfo = getOrder(statusRequest.getId());

			String trackingurl = statusRequest.getTrackingUrl();
			String trackingNo = statusRequest.getTrackingno();
			orderInfo.setTrackingURL(trackingurl);
			orderInfo.setTrackingNo(trackingNo);
			UserOrder updatedOrder = orderRepository.save(orderInfo);
			return updatedOrder;
		} catch (Exception e) {
			throw e;
		}

	}

	private Status getStatus(String status) {
		return Status.valueOf(status);
	}

	public List<?> getUserOrder(User user) {
		List<UserOrder> orderList = orderRepository.findByUser(user);
		List<UserOrder> paidOrderList = null;
		paidOrderList = orderList.stream().filter(o -> o.getPaymentStatus() != null || o.getPaymentMethod().equals("COD")).collect(Collectors.toList());
		return paidOrderList;
	}

	public Long getLastOrderNO() {
		List<UserOrder> userList = orderRepository.findAll();
		int Size = userList.size();
		if (Size == 0) {
			return (long) 0;
		}
		UserOrder userOrder = userList.get(Size - 1);
		Long init = (long) 1;
		return userOrder.getOrderNo() == null ? init : userOrder.getOrderNo();

	}

	public List<UserOrder> getAllCancelOrder() {
		List<UserOrder> userOrder = orderRepository.findByStatusOrderByCreatedAtAsc(Status.canceled);
		return userOrder;
	}

	public List<UserOrder> getUserCancelOrder(Long userId) {
		List<UserOrder> userOrder = orderRepository.findByStatusAndUserIdOrderByCreatedAtAsc(Status.canceled, userId);
		return userOrder;
	}

	public boolean amountVarify(double amount, List<Cart> cartItems) {
		totalAmount = 0;
		cartItems.forEach(cartinfo -> {
			System.out.println(cartinfo.getId());
			Cart cartItem = cartService.cartItemById(cartinfo.getId());
			double price = cartItem.getInventory().getPrice();
			price = price * cartItem.getQuantity();
			// get discount amount;
			Offer discount = null;
			List<Offer> productOffer = offerService.getOffersByProduct(cartItem.getInventory().getProduct().getId());
			if (productOffer.size() != 0) {
				discount = productOffer.get(0);
			}
//            List<Offer> categoryOffer = offerService.getOffersByCategory(cartItem.getInventory().getProduct().getCategory().getId());
//            if(categoryOffer.size() != 0 && discount == null){
//                discount = categoryOffer.get(0);
//            }
			totalAmount = totalAmount + price;
			if (discount != null) {
				int number = Integer.parseInt(discount.getAmount());
				
				if (discount.getDiscountType().equals("PERCENT")) {
					totalAmount = totalAmount - (totalAmount * number) / 100;
				} else {
					number = number * cartItem.getQuantity();
					totalAmount = totalAmount - number;
				}
			}
		});
		if (totalAmount != amount) {
			return false;
		}
		return true;
	}

	public boolean checkAmount(OrderRequest orderRequest) throws Exception {
		try {
			totalAmount = 0;
			Long productId = orderRequest.getProductId();
			int variantId = orderRequest.getVariantId();
			Inventory inventory = inventoryService.getProductVariantInventory(productId, variantId);
			if (inventory == null) {
				log.error("Product is not available");
				throw new Exception("Product is not available with id: " + productId);
			}
			double productPrice = inventory.getPrice();
			productPrice = productPrice * orderRequest.getQuantity();
			Offer discount = null;
			List<Offer> productOffer = offerService.getOffersByProduct(inventory.getProduct().getId());
			if (productOffer.size() != 0) {
				discount = productOffer.get(0);
			}
//             List<Offer> categoryOffer = offerService.getOffersByCategory(inventory.getProduct().getCategory().getId());
//             if(categoryOffer.size() != 0 && discount == null){
//                 discount = categoryOffer.get(0);
//             }
			totalAmount = totalAmount + productPrice;
			if (discount != null) {
				int number = Integer.parseInt(discount.getAmount());
				if (discount.getDiscountType().equals("PERCENT")) {
					totalAmount = totalAmount - (totalAmount * number) / 100;
				} else {
					number = number * orderRequest.getQuantity();
					totalAmount = totalAmount - number;
				}

			}
			if (totalAmount == orderRequest.getAmount()) {
				return true;
			} else {
				return false;
			}
		}

		catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}

	public boolean checkQuantity(Long productId, int variantId, int quantity) throws Exception {
		try {
			Inventory inventory = inventoryService.getProductVariantInventory(productId, variantId);
			if (inventory == null) {
				log.error("Product is not available with id: " + productId);
				throw new Exception("product is not available with id: " + productId);
			}
			if (inventory.getQuantity() >= quantity) {
				log.info("In Stock");
				return true;
			} else {
				log.error("Not in Stock");
				return false;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}

	// sending mail to owner
	@Async
	public void sendMailToAdminForOrder(UserOrder order) {
		try {
			ShippingAddress address = order.getShippingAddress();
			
			List<OrderDetails> orderDetails = orderDetailsRepository.findByUesrOrder(order);
			final String subject = "Order placed";
			String emailMessage="<p style=\"text-align: center;\"><span style=\"font-size: 8pt;\"><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"https://geonix.in/assets/images/geonix-logo.webp\" width=\"93\" height=\"93\"></span>**Ordered Recieved*</p>\r\n"
					+ "<p style=\"text-align: left;\">Shipping Details</p>\r\n"
					+ "<table style=\"border-collapse: collapse; width: 100%;\" border=\"1\">\r\n"
					+ "<tbody>\r\n"
					+ "<tr>\r\n"
					+ "<td style=\"width: 49.375%;\"><span style=\"font-size: 10pt;\">Name</span></td>\r\n"
					+ "<td style=\"width: 49.375%;\">"+address.getName()+"</td>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td style=\"width: 49.375%;\"><span style=\"font-size: 10pt;\">Country</span></td>\r\n"
					+ "<td style=\"width: 49.375%;\">"+address.getCountry()+"</td>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td style=\"width: 49.375%;\"><span style=\"font-size: 10pt;\">Street</span></td>\r\n"
					+ "<td style=\"width: 49.375%;\">"+address.getStreet()+"</td>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td style=\"width: 49.375%;\"><span style=\"font-size: 10pt;\">State</span></td>\r\n"
					+ "<td style=\"width: 49.375%;\">"+address.getState()+"</td>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td style=\"width: 49.375%;\"><span style=\"font-size: 10pt;\">City</span></td>\r\n"
					+ "<td style=\"width: 49.375%;\">"+address.getCity()+"</td>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td style=\"width: 49.375%;\"><span style=\"font-size: 10pt;\">Pincode</span></td>\r\n"
					+ "<td style=\"width: 49.375%;\">"+address.getPincode()+"</td>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td style=\"width: 49.375%;\"><span style=\"font-size: 10pt;\">Mobile</span></td>\r\n"
					+ "<td style=\"width: 49.375%;\">"+address.getMobile()+"</td>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td style=\"width: 49.375%;\"><span style=\"font-size: 10pt;\">Email</span></td>\r\n"
					+ "<td style=\"width: 49.375%;\">"+address.getEmail()+"</td>\r\n"
					+ "</tr>\r\n"
					+ "</tbody>\r\n"
					+ "</table>\r\n"
					+ "<p>Order Details</p>\r\n"
					+ "<table style=\"border-collapse: collapse; width: 100%; height: 36px;\" border=\"1\">\r\n"
					+ "<tbody>\r\n"
					+ "<tr style=\"height: 18px;\">\r\n"
					+ "<td style=\"width: 16.0993%; height: 18px;\"><span style=\"font-size: 10pt;\">Product Name</span></td>\r\n"
					+ "<td style=\"width: 16.0993%; height: 18px;\"><span style=\"font-size: 10pt;\">Quantity</span></td>\r\n"
					+ "<td style=\"width: 16.0993%; height: 18px;\"><span style=\"font-size: 10pt;\">OrderId</span></td>\r\n"
					+ "<td style=\"width: 16.0993%; height: 18px;\"><span style=\"font-size: 10pt;\">Order Amount</span></td>\r\n"
					+ "<td style=\"width: 16.0993%; height: 18px;\"><span style=\"font-size: 10pt;\">Order Status</span></td>\r\n"
					+ "<td style=\"width: 16.1105%; height: 18px;\"><span style=\"font-size: 10pt;\">Payment Method</span></td>\r\n"
					+ "</tr>\r\n";
					
				
				for(OrderDetails detail : orderDetails) {
					emailMessage += ""
							+ "<tr style=\"height: 18px;\">\r\n"
							+ "<td style=\"width: 16.0993%; height: 18px;\">"+detail.getProduct().getName()+"</td>\r\n"
							+ "<td style=\"width: 16.0993%; height: 18px;\">"+detail.getQuantity()+"</td>\r\n"
							+ "<td style=\"width: 16.0993%; height: 18px;\">"+detail.getUesrOrder().getOrderNo()+"</td>\r\n"
							+ "<td style=\"width: 16.0993%; height: 18px;\">"+detail.getPrice()+"</td>\r\n"
							+ "<td style=\"width: 16.0993%; height: 18px;\">"+detail.getUesrOrder().getStatus()+"</td>\r\n"
							+ "<td style=\"width: 16.1105%; height: 18px;\">"+detail.getUesrOrder().getPaymentMethod()+"</td>\r\n"
							+ "</tr>\r\n";
							
				}
				emailMessage +=  "</tbody>\r\n"
						+ "</table>";
					
				
			final String message1 = "************ ORDER PLACED ************ \r\n \r\n Thank you for"
					+ " ordering from Geonix. \r\n Your order "+order.getOrderNo()+" has been placed.\r\n"
							+ " # SHIPPING DETAILS # \r\n" + "Name : "
					+ address.getName() + "\r\n Country : " + address.getCountry() + "\r\n Street : "
					+ address.getStreet() + "\r\n State : " + address.getState() + "\r\n City : " + address.getCity()
					+ "\r\n Pincode : " + address.getPincode() + "\r\n Mobile : " + address.getMobile()
					+ "\r\n Email : " + order.getUser().getEmail() + "\r\n \r\n" + "# ORDER DETAILS # "
					+ "\r\n Order ID : " + order.getOrderNo() + "\r\n Order Amount : " + order.getAmount()
					+ "\r\n Order Status : " + order.getStatus().toString() + "\r\n Payment Method : "+ order.getPaymentMethod();
			   

			      // Sender's email ID needs to be mentioned
			      String from = "geonixshopping@gmail.com";
			      final String username = "geonixshopping@gmail.com";//change accordingly
			      final String password = "jeyrsykrxshadkeq";//change accordingly

			      // Assuming you are sending email through relay.jangosmtp.net
			      String host = "smtp.gmail.com";

			      Properties props = new Properties();
			      props.put("mail.smtp.auth", "true");
			      props.put("mail.smtp.starttls.enable", "true");
			      props.put("mail.smtp.host", host);
			      props.put("mail.smtp.port", "587");

			      // Get the Session object.
			      Session session = Session.getInstance(props,
			         new javax.mail.Authenticator() {
			            protected PasswordAuthentication getPasswordAuthentication() {
			               return new PasswordAuthentication(username, password);
			            }
				});

			      try {
			            // Create a default MimeMessage object.
			            Message message = new MimeMessage(session);

			   	   // Set From: header field of the header.
				   message.setFrom(new InternetAddress(from));

				   // Set To: header field of the header.
				   message.setRecipients(Message.RecipientType.TO,
			              InternetAddress.parse("geonixshopping@gmail.com"));

				   // Set Subject: header field
				   message.setSubject("Order Recieved");

				   // Send the actual HTML message, as big as you like
				   message.setContent(
			             emailMessage,
			             "text/html");

				   // Send message
				   Transport.send(message);

				   System.out.println("Sent message successfully....");

			      } catch (MessagingException e) {
				   e.printStackTrace();
				   throw new RuntimeException(e);
			      }
			
		} catch (Exception e) {
			log.error("Error occured while sending mail to admin for order: " + e.getMessage());
			throw e;
		}

	}
	
	
	//sending mail to buyer
	@Async
	public void sendMailToBuyerForOrder(UserOrder order) {
		try {
			ShippingAddress address = order.getShippingAddress();
			
			List<OrderDetails> orderDetails = orderDetailsRepository.findByUesrOrder(order);
			final String subject = "Order placed";
			String emailMessage="<p style=\"text-align: center;\"><span style=\"font-size: 8pt;\"><img style=\"display: block; margin-left: auto; margin-right: auto;\" src=\"https://geonix.in/assets/images/geonix-logo.webp\" width=\"93\" height=\"93\"></span>**Ordered Recieved*</p>\r\n"
					+ "<p style=\"text-align: left;\">Shipping Details</p>\r\n"
					+ "<table style=\"border-collapse: collapse; width: 100%;\" border=\"1\">\r\n"
					+ "<tbody>\r\n"
					+ "<tr>\r\n"
					+ "<td style=\"width: 49.375%;\"><span style=\"font-size: 10pt;\">Name</span></td>\r\n"
					+ "<td style=\"width: 49.375%;\">"+address.getName()+"</td>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td style=\"width: 49.375%;\"><span style=\"font-size: 10pt;\">Country</span></td>\r\n"
					+ "<td style=\"width: 49.375%;\">"+address.getCountry()+"</td>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td style=\"width: 49.375%;\"><span style=\"font-size: 10pt;\">Street</span></td>\r\n"
					+ "<td style=\"width: 49.375%;\">"+address.getStreet()+"</td>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td style=\"width: 49.375%;\"><span style=\"font-size: 10pt;\">State</span></td>\r\n"
					+ "<td style=\"width: 49.375%;\">"+address.getState()+"</td>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td style=\"width: 49.375%;\"><span style=\"font-size: 10pt;\">City</span></td>\r\n"
					+ "<td style=\"width: 49.375%;\">"+address.getCity()+"</td>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td style=\"width: 49.375%;\"><span style=\"font-size: 10pt;\">Pincode</span></td>\r\n"
					+ "<td style=\"width: 49.375%;\">"+address.getPincode()+"</td>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td style=\"width: 49.375%;\"><span style=\"font-size: 10pt;\">Mobile</span></td>\r\n"
					+ "<td style=\"width: 49.375%;\">"+address.getMobile()+"</td>\r\n"
					+ "</tr>\r\n"
					+ "<tr>\r\n"
					+ "<td style=\"width: 49.375%;\"><span style=\"font-size: 10pt;\">Email</span></td>\r\n"
					+ "<td style=\"width: 49.375%;\">"+address.getEmail()+"</td>\r\n"
					+ "</tr>\r\n"
					+ "</tbody>\r\n"
					+ "</table>\r\n"
					+ "<p>Order Details</p>\r\n"
					+ "<table style=\"border-collapse: collapse; width: 100%; height: 36px;\" border=\"1\">\r\n"
					+ "<tbody>\r\n"
					+ "<tr style=\"height: 18px;\">\r\n"
					+ "<td style=\"width: 16.0993%; height: 18px;\"><span style=\"font-size: 10pt;\">Product Name</span></td>\r\n"
					+ "<td style=\"width: 16.0993%; height: 18px;\"><span style=\"font-size: 10pt;\">Quantity</span></td>\r\n"
					+ "<td style=\"width: 16.0993%; height: 18px;\"><span style=\"font-size: 10pt;\">OrderId</span></td>\r\n"
					+ "<td style=\"width: 16.0993%; height: 18px;\"><span style=\"font-size: 10pt;\">Order Amount</span></td>\r\n"
					+ "<td style=\"width: 16.0993%; height: 18px;\"><span style=\"font-size: 10pt;\">Order Status</span></td>\r\n"
					+ "<td style=\"width: 16.1105%; height: 18px;\"><span style=\"font-size: 10pt;\">Payment Method</span></td>\r\n"
					+ "</tr>\r\n";
					
				
				for(OrderDetails detail : orderDetails) {
					emailMessage += ""
							+ "<tr style=\"height: 18px;\">\r\n"
							+ "<td style=\"width: 16.0993%; height: 18px;\">"+detail.getProduct().getName()+"</td>\r\n"
							+ "<td style=\"width: 16.0993%; height: 18px;\">"+detail.getQuantity()+"</td>\r\n"
							+ "<td style=\"width: 16.0993%; height: 18px;\">"+detail.getUesrOrder().getOrderNo()+"</td>\r\n"
							+ "<td style=\"width: 16.0993%; height: 18px;\">"+detail.getPrice()+"</td>\r\n"
							+ "<td style=\"width: 16.0993%; height: 18px;\">"+detail.getUesrOrder().getStatus()+"</td>\r\n"
							+ "<td style=\"width: 16.1105%; height: 18px;\">"+detail.getUesrOrder().getPaymentMethod()+"</td>\r\n"
							+ "</tr>\r\n";
							
				}
				emailMessage +=  "</tbody>\r\n"
						+ "</table>";
					
				
			final String message1 = "************ ORDER PLACED ************ \r\n \r\n Thank you for"
					+ " ordering from Geonix. \r\n Your order "+order.getOrderNo()+" has been placed.\r\n"
							+ " # SHIPPING DETAILS # \r\n" + "Name : "
					+ address.getName() + "\r\n Country : " + address.getCountry() + "\r\n Street : "
					+ address.getStreet() + "\r\n State : " + address.getState() + "\r\n City : " + address.getCity()
					+ "\r\n Pincode : " + address.getPincode() + "\r\n Mobile : " + address.getMobile()
					+ "\r\n Email : " + order.getUser().getEmail() + "\r\n \r\n" + "# ORDER DETAILS # "
					+ "\r\n Order ID : " + order.getOrderNo() + "\r\n Order Amount : " + order.getAmount()
					+ "\r\n Order Status : " + order.getStatus().toString() + "\r\n Payment Method : "+ order.getPaymentMethod();
			   

			      // Sender's email ID needs to be mentioned
			      String from = "geonixshopping@gmail.com";
			      final String username = "geonixshopping@gmail.com";//change accordingly
			      final String password = "jeyrsykrxshadkeq";//change accordingly

			      // Assuming you are sending email through relay.jangosmtp.net
			      String host = "smtp.gmail.com";

			      Properties props = new Properties();
			      props.put("mail.smtp.auth", "true");
			      props.put("mail.smtp.starttls.enable", "true");
			      props.put("mail.smtp.host", host);
			      props.put("mail.smtp.port", "587");

			      // Get the Session object.
			      Session session = Session.getInstance(props,
			         new javax.mail.Authenticator() {
			            protected PasswordAuthentication getPasswordAuthentication() {
			               return new PasswordAuthentication(username, password);
			            }
				});

			      try {
			            // Create a default MimeMessage object.
			            Message message = new MimeMessage(session);

			   	   // Set From: header field of the header.
				   message.setFrom(new InternetAddress(from));

				   // Set To: header field of the header.
				   message.setRecipients(Message.RecipientType.TO,
			              InternetAddress.parse(order.getUser().getEmail()));

				   // Set Subject: header field
				   message.setSubject("Order Recieved");

				   // Send the actual HTML message, as big as you like
				   message.setContent(
			             emailMessage,
			             "text/html");

				   // Send message
				   Transport.send(message);

				   System.out.println("Sent message successfully....");
					  OutputStream fileOutputStream = null;
					  try {
						  fileOutputStream = new FileOutputStream(order.getId()+".pdf");
					  } catch (FileNotFoundException e) {
						  throw new RuntimeException(e);
					  }
					  HtmlConverter.convertToPdf(emailMessage, fileOutputStream);
					  File file = new File(order.getId()+".pdf");
					  String invoiceUrl = awss3Service.uploadinvoicetos3("geonix",file,order).toString();
			     	UserOrder userOrder =	orderRepository.findById(order.getId()).get();
					userOrder.setInvoiceURL(invoiceUrl);
					orderRepository.save(userOrder);
			      } catch (MessagingException e) {
				   e.printStackTrace();
				   throw new RuntimeException(e);
			      }
			
		} catch (Exception e) {
			log.error("Error occured while sending mail to admin for order: " + e.getMessage());
			throw e;
		}
	}

	// create guest user
	public User createGuestUser(ShippingAddress shippingAddress) {
		try {
			log.info("Creating User...");
			User user = new User();
			String[] name = shippingAddress.getName().split(" ");
			user.setFirstName(name[0]);
			user.setLastName(name[1]);
			user.setEmail(shippingAddress.getEmail());
			user.setPassword(name[1] + name[0]);
			user.setPhoneNo(shippingAddress.getMobile());
			user.setEnabled(true);
			UserProfile userProfile = new UserProfile();
			userProfile.setFirstName(name[0]);
			userProfile.setLastName(name[1]);
			userProfile.setAddress(shippingAddress.getStreet());
			userProfile.setCity(shippingAddress.getCity());
			userProfile.setCountry(shippingAddress.getCountry());
			userProfile.setEmail(shippingAddress.getEmail());
			userProfile.setPhone(shippingAddress.getMobile());
			user.setUserProfile(profileRepo.save(userProfile));
			log.info("User created with profile");
			return userRepository.save(user);
			
			

		} catch (Exception e) {
			throw e;
		}
	}
	public String createGuestToken(String email) {
		try {
			return jwtTokenUtil.createToken(email);
		}
		catch(Exception e) {
			throw e;
		}
	}
	public Map<String ,String> guestInfo(User user){
		try {
			 Map<String,String> guestInfo=new HashMap<>();
             guestInfo.put("guestId",user.getId().toString());
             guestInfo.put("guestEmail",user.getEmail());
             guestInfo.put("guestToken",createGuestToken(user.getEmail()));
             return guestInfo;
		}
		catch(Exception e) {
			throw e;
		}
	}
		public static void main(String[] args) {
		      // Recipient's email ID needs to be mentioned.
		      String to = "anuragpundir641@gmail.com";

		      // Sender's email ID needs to be mentioned
		      String from = "geonixshopping@gmail.com";
		      final String username = "geonixshopping@gmail.com";//change accordingly
		      final String password = "jeyrsykrxshadkeq";//change accordingly

		      // Assuming you are sending email through relay.jangosmtp.net
		      String host = "smtp.gmail.com";

		      Properties props = new Properties();
		      props.put("mail.smtp.auth", "true");
		      props.put("mail.smtp.starttls.enable", "true");
		      props.put("mail.smtp.host", host);
		      props.put("mail.smtp.port", "587");

		      // Get the Session object.
		      Session session = Session.getInstance(props,
		         new javax.mail.Authenticator() {
		            protected PasswordAuthentication getPasswordAuthentication() {
		               return new PasswordAuthentication(username, password);
		            }
			});

		      try {
		            // Create a default MimeMessage object.
		            Message message = new MimeMessage(session);

		   	   // Set From: header field of the header.
			   message.setFrom(new InternetAddress(from));

			   // Set To: header field of the header.
			   message.setRecipients(Message.RecipientType.TO,
		              InternetAddress.parse(to));

			   // Set Subject: header field
			   message.setSubject("Testing Subject");

			   // Send the actual HTML message, as big as you like
			   message.setContent(
		              "<h1>This is actual message embedded in HTML tags</h1>",
		             "text/html");

			   // Send message
			   Transport.send(message);

			   System.out.println("Sent message successfully....");

		      } catch (MessagingException e) {
			   e.printStackTrace();
			   throw new RuntimeException(e);
		      }
		   }
	

}
