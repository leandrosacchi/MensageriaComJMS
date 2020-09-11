package br.com.caelum.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TesteConsumidorFila {

	public static void main(String[] args) throws Exception {
			
		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
		connection.start();
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE); 
		//A Session abstrai o trabalho transacional e de confirmação do recebimento da mensagem.
		//false = não quero uma transação
		//AUTO_ACKNOWLEDGE = confirma automaticamente o recebimento da mensagem
		
		Destination fila = (Destination) context.lookup("financeiro");//local concreto onde a mensagem será salva temporariamente ,dentro do MOM
		MessageConsumer consumer = session.createConsumer(fila); //consumer fica escutando as mensagens de uma fila concreta
					
//		Message message = consumer.receive(); //recebedor da mensagem devolve uma mensagem;
		
		consumer.setMessageListener(new MessageListener() { 
			//MessageListener permite que o consumer fique escutando e não termine quando recebe uma mensagem
			
			@Override
			public void onMessage(Message message) {
				TextMessage textMessage = (TextMessage) message;
				try {
					System.out.println(textMessage.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		
		new Scanner(System.in).nextLine();
		
		session.close();
		connection.close();
		context.close();
	}

}
