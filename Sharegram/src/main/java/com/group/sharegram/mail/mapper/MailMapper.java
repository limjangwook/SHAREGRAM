package com.group.sharegram.mail.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.group.sharegram.addr.domain.EmpAddrDTO;
import com.group.sharegram.mail.domain.MailAtchDTO;
import com.group.sharegram.mail.domain.MailDTO;
import com.group.sharegram.mail.domain.ReceiversDTO;
import com.group.sharegram.user.domain.EmployeesDTO;
import com.group.sharegram.mail.domain.MailSummerImageDTO;

@Mapper
public interface MailMapper {
	public int insertJamesUser(EmpAddrDTO empInfo);
	public MailDTO selectMailByMap(Map<String, Object> map);
	public int insertMail(MailDTO mail);
	public int insertReceivers(Map<String, Object> map);
	public int selectReceiveMailCount(Map<String, Object> map);
	public List<MailDTO> selectReceiveMailList(Map<String, Object> map);
	public int selectSendMailCount(Map<String, Object> map);
	public List<MailDTO> selectSendMailList(Map<String, Object> map);
	public int selectReadNotReceiveCount(Map<String, Object> map);
	public List<ReceiversDTO> selectReceiverList(int mailNo);
	public ReceiversDTO selectReceiverByMap(Map<String, Object> map);
	public ReceiversDTO selectSendReceiverByMap(Map<String, Object> map);
	public int updateCheckByMap(Map<String, Object> map);
	public int selectReadNotSendCount(int empNo);
	public int[] selectReceiveEmp(int mailNo);
	public int insertMailSummerImage(MailSummerImageDTO summernote);
	public MailSummerImageDTO selectMailSummernoteImageListInMail(int mailNo);
	public MailSummerImageDTO selectAllMailSummernoteImageList();
	public int deleteMailSummernoteImage(String filesystem);
	public int insertMailAttach(MailAtchDTO attach);
	public List<MailAtchDTO> selectMailAttachList(int mailNo);
	public MailAtchDTO selectMailAttachByNo(int fileNo);
	public int insertMailAttachByFileNo(MailAtchDTO attach);
	public int deleteReceiver(Map<String, Object> map);
	public MailAtchDTO selectMailAttachByFileNo(int fileNo);
	public List<EmployeesDTO> selectAllEmpList();
}
