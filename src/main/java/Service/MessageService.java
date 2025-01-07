package Service;

import Model.Message;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public Message createMesssage(Message message){
        if (message.getMessage_text().isBlank() ||
        message.getMessage_text().length() > 255 ||
        accountDAO.getAccountById(message.getPosted_by()) == null) {
            return null;
        }
        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        if(messageDAO.getMessageById(messageId) == null){
            return null;
        }
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessage(int messageId) {
        if(messageDAO.getMessageById(messageId) == null){
            return null;
        }
        return messageDAO.deleteMessage(messageId);
    }

    public Message updateMessage(int messageId, String newText) {
        if (newText.isBlank() || newText.length() > 255) {
            return null;
        }
        return messageDAO.updateMessage(messageId, newText);
    }

    public List<Message> getMessagesByUser(int accountId) {
        return messageDAO.getMessagesByUser(accountId);
    }

}
