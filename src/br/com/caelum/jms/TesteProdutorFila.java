package br.com.caelum.jms;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TesteProdutorFila {

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
		
		MessageProducer producer = session.createProducer(fila);
		
		
		for (int i = 0; i < 100; i++) {
			Message message = session.createTextMessage("<pedido><id>"+i+"</id></pedido>");
			producer.send(message);
			System.out.println("Mensagem ["+(i+1)+"] enviada: "+message);			
		}
		
		new Scanner(System.in).nextLine();
		
		session.close();
		connection.close();
		context.close();
	}

}
