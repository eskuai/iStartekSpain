package server.dataIn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@ToString
public class DataInBean {
	String headerType;
	String lenPacket ;
	String idPacket;
	String command;
	String data;
	String checkSum;
	String packetEOT;
	
	public DataInBean (String hexStr) {
		log.info(hexStr);
		headerType = hexStr.substring(0, 4);
		lenPacket = hexStr.substring(4, 8);
		idPacket = hexStr.substring(8, 22);
		command = hexStr.substring(22, 26);
	}
}

//  "242400900000001FFFFFFF99553134333334352E3030302C412C3430
