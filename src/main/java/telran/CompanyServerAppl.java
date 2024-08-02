package telran;

import telran.employees.*;
import telran.io.Persistable;
import telran.net.Protocol;
import telran.net.TcpServer;

public class CompanyServerAppl {

	private static final String FILE_NAME = "employeesTest.data";
	private static final int PORT = 8000;

	public static void main(String[] args) {
		
		Company company = new CompanyMapsImpl();
		try {
			((Persistable)company).restore(FILE_NAME);
		} catch (Exception e) {
			
		}
		Protocol protocol = new CompanyProtocol(company);
		TcpServer tcpServer = new TcpServer(protocol, PORT);
		//FIXME need to start TCPServer as a thread
		tcpServer.run();
		//TODO
		//cycle with asking a user to enter shutdown for exit from the server
		//regular while cycle with no using cli-view
		//by entering "shutdown" you should call method shutdown of the TcpServer
		//after shutdown you should perform saving the data into the file

	}

}