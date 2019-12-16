package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class EmailController {

    @Autowired
    SendEmail sendEmail;

    @GetMapping("/addEmail/{id}")
    public String departmentForm(Model model,
                                 @PathVariable("id") long userId){
        model.addAttribute("email", new EmailSet(userId));
        model.addAttribute("userId", userId);
        return "emailform";
    }

    @RequestMapping("/processEmail")
    public String processEmailForm(@ModelAttribute EmailSet emailSet,
                                   Model model,
                                   @RequestParam(value = "reasonText", required = false) String reasonText){

        // EmailSet object can have a long userId field
        // when you make the NEW emailset object, below, initialize it with the userId (constructor)
        // this means the "email" that the form receives, already knows what it needs to know for later...
        // the object now consists of a message and a user id. that's great! because later, when it comes back,
        // @ModelAttribute... you can reach inside the emailSet object, and grab that userId that you need...
        // and do whatever you need to do with it...


//        EmailSet email = new EmailSet(userId);
//        email.setReasonText(reasonText);


        System.out.println("My Email Set object has user Id = " + emailSet.getUserId());
        System.out.println("My Email Set Object has reason = " + emailSet.getReasonText());
        sendEmail.SendSimpleEmail(model, emailSet.getUserId(), emailSet);

        System.out.println(reasonText);
        return "redirect:/";
    }

    @RequestMapping("/sendemail/{id}")
    public String sendEmail(@PathVariable("id") long userId,
                            EmailSet emailSet,
                            Model model) {


        model = sendEmail.SendSimpleEmail(model, userId, emailSet);
        return "confirmemail";
    }





}


   /* Before post mapping
    //Send email to the user that is currently logged in - I guess in this case the Supervisor
    @RequestMapping("/sendemail/{id}")
    public String sendEmail(@PathVariable("id") long userId,
                            Model model,
                            @RequestParam(value = "reasonText", required = false) String reasonText) {


        EmailSet email = new EmailSet();
        email.setReasonText(reasonText);
        model = sendEmail.SendSimpleEmail(model, userId, email);
        return "emailform";
    }


     */
//***add a process email
   /*
    @GetMapping("/addEmail")
    public String departmentForm(Model model){

        return "emailform";
    }



    @PostMapping("/processEmail")
    public String processEmailForm(@ModelAttribute EmailSet emailSet){
        return "redirect:/";
    }
     */