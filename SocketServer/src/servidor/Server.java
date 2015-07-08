package servidor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class Server {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		// Formatacao de hora utilizada
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

		// array de bytes para recepção de msgs
		byte[] dados_in = new byte [1000];
		
		// Escuta conexão na porta 8200
		ServerSocket servidor = new ServerSocket(8200);
		
		//Loop infinito para o processamento das conexoes	
		while (true){
			
			System.out.println("\nAguadando conexão...");
		// Bloqueia até receber requisicao de conexao	
			Socket cliente = servidor.accept();	
			 
			
			// pega data/hora do sistema
			Date datahora = new Date();  
			calendar.setTime(datahora);
		
			//Mostra IP do rastreador
		     System.out.println("\n\nData: " + format.format(calendar.getTime())+"\nCliente: " + cliente.getInetAddress().getHostAddress());
		     
		     // String que mostra os dados recebidos do rastreador
		     StringBuilder s = new StringBuilder();
		     
			 // Cria uma buffer que irá armazenar as informações enviadas pelo cliente
			// BufferedReader inFromClient = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
		     
		     
			 try {
			
			 //Recebe os dados do rastreador
			 int len = new DataInputStream(cliente.getInputStream()).read(dados_in);
			 
			 //Total de bytes recebidos
			 System.out.println("\nTotal de bytes recebidos: "+ len);		

			 
			 // Loop para gerar string de visualização dos dados recebidos
			 for (int i = 0; i < len; i++) {
				 
				 int parteAlta = ((dados_in[i] >> 4) & 0xf) << 4;
		            int parteBaixa = dados_in[i] & 0xf;
		            if (parteAlta == 0) s.append('0');
		            s.append(Integer.toHexString(parteAlta | parteBaixa));
				   // s.append(Integer.toHexString(dados[i]));
				    s.append(" ");
				}
			 //------------------------------------------------------------
			 
			 } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
			 //Exibe dado recebido
			 
			  System.out.println("\nRecebido: "+ s);//clientSentence);
			  
			  
			  if ((dados_in[11] == 0x50) && (dados_in[12] == 0x00)){
			  
			  //resposta de login
			  byte[] bytes = {0x40,0x40,0x00,0x12,0x01,0x19,0x70,(byte) 0x94,(byte) 0x93,0x48,(byte) 0xff, 0x40, 0x00, 0x01};
			//	  byte[] bytes = {0x40,0x40,0x00,0x11,0x01,0x19,0x70,(byte) 0x94,(byte) 0x93,0x48,(byte) 0xff, (byte) 0x99, 0x55};
			  int checksum = CRC(bytes);
			  
			  byte[] envio = new byte [bytes.length+ 4];
			  envio = Arrays.copyOf(bytes, bytes.length+4);
			  
			  envio[bytes.length] =  (byte) ((checksum & 0xff00) >> 8);
			  envio[bytes.length+1] =  (byte) (checksum & 0x00ff);
/*			  System.out.println("CRC= " + Integer.toHexString(checksum));
			  System.out.println("nxt= " + Integer.toHexString(envio[bytes.length]));
			  System.out.println("nxy= " + Integer.toHexString(envio[bytes.length+1]));
*/			  
			  envio[bytes.length+2] = 0x0d;
			  envio[bytes.length+3] = 0x0a;
			  
			  
			  
		     // Cria uma stream de sáida para retorno das informações ao cliente
             DataOutputStream outToClient = new DataOutputStream(cliente.getOutputStream());
             outToClient.write(envio);
			 
             // Faz a leitura das informações enviadas pelo cliente as amazenam na variável "clientSentence"
             //  String  clientSentence = inFromClient.readLine();
             StringBuilder s1 = new StringBuilder();
             for (int i = 0; i < envio.length; i++) {
				 
				 int parteAlta = ((envio[i] >> 4) & 0xf) << 4;
		            int parteBaixa = envio[i] & 0xf;
		            if (parteAlta == 0) s.append('0');
		            s1.append(Integer.toHexString(parteAlta | parteBaixa));
				   // s.append(Integer.toHexString(dados[i]));
				    s1.append(" ");
				}
             
             System.out.println("\nEnviado: "+ s1);//clientSentence);
			  }
             
			   StringBuilder s2 = new StringBuilder();
				 try {
						
					 //Recebe os dados do rastreador
					 int len = new DataInputStream(cliente.getInputStream()).read(dados_in);
					 
					 //Total de bytes recebidos
					 System.out.println("\nTotal de bytes recebidos: "+ len);	
					 
					 

					 System.out.println("Coordenada recebida: ");		

					 // Loop para gerar string de visualização dos dados recebidos
					 for (int i = 0; i < len; i++) {
						 System.out.print( (char) dados_in[i]);
						 s2.append(dados_in[i]);
			/*			 int parteAlta = ((dados_in[i] >> 4) & 0xf) << 4;
				            int parteBaixa = dados_in[i] & 0xf;
				            if (parteAlta == 0) s2.append('0');
				            s2.append(Integer.toHexString(parteAlta | parteBaixa));
						   // s.append(Integer.toHexString(dados[i]));
						    s2.append(" ");
*/						}
					 //------------------------------------------------------------
					 
					 } catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 
					 //Exibe dado recebido
				//	  System.out.println("\nRecebido: "+ s2);//clientSentence);

			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
			  
            cliente.close();
            
            
             
		}
		

		
		
		
	}
		
	
	/************************************************************************************
	 * 
	 *  Rotina de cálculo do CRC 
	 * 
	 * 
	 *************************************************************************************/
	
	public static int CRC(byte[] bytes){
		
		
		 int crc = 0xFFFF;          // valor inicial
	     int polynomial = 0x1021;   //tipo do polinomio-- 0001 0000 0010 0001  (0, 5, 12) 

	      //  byte[] bytes = {0x24,0x24,0x00,0x11,0x13,0x61,0x23,0x45,0x67,(byte) 0x8f,(byte) 0xff, 0x50, 0x00};

	        for (byte b : bytes) {
	            for (int i = 0; i < 8; i++) {
	                boolean bit = ((b   >> (7-i) & 1) == 1);
	                boolean c15 = ((crc >> 15    & 1) == 1);
	                crc <<= 1;
	                if (c15 ^ bit) crc ^= polynomial;
	             }
	        }

	        crc &= 0xffff;
	       // System.out.println("CRC16-CCITT = " + Integer.toHexString(crc));
			return crc;
	}
	    

	}


