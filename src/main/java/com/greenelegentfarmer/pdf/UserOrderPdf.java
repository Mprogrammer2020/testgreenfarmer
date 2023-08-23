package com.greenelegentfarmer.pdf;

import java.io.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.greenelegentfarmer.entity.UserOrder;
import com.greenelegentfarmer.service.UserOrderService;
import com.itextpdf.html2pdf.HtmlConverter;

@Controller
@RequestMapping("/user-order")
public class UserOrderPdf {

	@Autowired
	private UserOrderService userOrderService;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@GetMapping("/pdf/{id}")
    public String pdfPage(Model model,@PathVariable Long id) throws Exception {
        model.addAttribute("order", userOrderService.expect(id));
        return "pdf/order";
    }
	
	@GetMapping("/pdf/parse/{id}")
	public ResponseEntity<?> pdfFile(@PathVariable Long id) throws Exception {
		UserOrder order = userOrderService.expect(id);

	    Context context=new Context();
	    	context.setVariable("order", order);
	    String orderHtml = templateEngine.process("pdf/order", context);

	    ByteArrayOutputStream target = new ByteArrayOutputStream();
	    HtmlConverter.convertToPdf(orderHtml, target);

	    return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(target.toByteArray());

	}
}
