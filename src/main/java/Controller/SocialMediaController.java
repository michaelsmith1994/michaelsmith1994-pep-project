package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;
import java.util.logging.Handler;

import org.eclipse.jetty.websocket.core.internal.MessageHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import net.bytebuddy.utility.visitor.ContextClassVisitor;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        //Account
        app.post("/register", this::registerAccountHand);
        app.post("/login", this::loginAccountHand);
        //Message
        app.post("/messages", this::createMessageHand);//create
        app.get("/messages", this::getAllMessagesHand);//get all
        app.get("/messages/{message_id}", this::getMessageByIDHand);//get by ID
        app.delete("/messages/{message_id}", this::deleteMessageHand);
        app.patch("/messages/{message_id}", this::updateMessageHand);
        app.get("accounts/{account_id}/messages", this::getUserMessagesHand);

        return app.stop();
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerAccountHand(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account regAccount = accountService.registerAccount(account);
        if(regAccount != null){
            ctx.json(mapper.writeValueAsString(regAccount));
        }else{
            ctx.status(400);
        }
    }
    private void loginAccountHand(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account logAccount = accountService.login(account);
        if(logAccount != null){
            ctx.json(mapper.writeValueAsString(logAccount));
        }else{
            ctx.status(401);
        }
    }
    private void createMessageHand(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = messageService.createMesssage(message);
        if(newMessage != null){
            ctx.json(mapper.writeValueAsString(newMessage));
        }else{
            ctx.status(400);
        }
    }
    private void getAllMessagesHand(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }
    private void getMessageByIDHand(Context ctx) {
        try {
            int messagePK = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessageById(messagePK);
            if (message != null) {
                ctx.json(message);
            }
        } catch (NumberFormatException e) {
            ctx.status(400);
        }
    }
    private void deleteMessageHand(Context ctx) {
        try {
            int messagePK = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.deleteMessage(messagePK);
            if (message != null) {
                ctx.json(message);
            } else {
                ctx.status(200);
                // Don't set any response body at all
            }
        } catch (NumberFormatException e) {
            ctx.status(400);
        }
    }

    private void updateMessageHand(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        int messagePK = Integer.parseInt(ctx.pathParam("message_id"));
        Message oldMessage = mapper.readValue(ctx.body(), Message.class);
        Message updatedMessage = messageService.updateMessage(messagePK, oldMessage.getMessage_text());
        if(updatedMessage != null){
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }else{
            ctx.status(400);
        }
        
    }
    private void getUserMessagesHand(Context ctx) {
        int accountPK = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUser(accountPK);
        ctx.json(messages);
    }

}