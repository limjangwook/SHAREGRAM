package com.group.sharegram.mail.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.group.sharegram.addr.domain.EmpAddrDTO;
import com.group.sharegram.addr.mapper.AddressMapper;
import com.group.sharegram.mail.domain.MailAtchDTO;
import com.group.sharegram.mail.domain.MailDTO;
import com.group.sharegram.mail.domain.ReceiversDTO;
import com.group.sharegram.mail.domain.MailSummerImageDTO;
import com.group.sharegram.mail.mapper.MailMapper;
import com.group.sharegram.mail.util.MailIUtil;
import com.group.sharegram.mail.util.MyFileMailUtil;
import com.group.sharegram.mail.util.MailPageUtil;
import com.group.sharegram.user.domain.EmployeesDTO;
import com.group.sharegram.user.mapper.EmpMapper;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class MailServiceImpl implements MailService {
	
	@Autowired
	private MailIUtil mailUtil;
	
	@Autowired
	private MailMapper mailMapper;
	
	@Autowired
	private EmpMapper empMapper;
	
	@Autowired
	private MailPageUtil pageUtil;
	
	@Autowired
	private AddressMapper addrMapper;
	
	@Autowired
	private MyFileMailUtil myFileUtil;
	
	@Transactional
	@Override
	public Map<String, Object> saveMail(MultipartHttpServletRequest multipartRequest, MailDTO mail) {
		EmpAddrDTO mailUser = (EmpAddrDTO)multipartRequest.getSession().getAttribute("mailUser");
		int empNo = mailUser.getEmpNo();
		mail.setEmpNo(empNo);
		
		//System.out.println(mail.getStrTo());
		
		mail.setToAddr(mail.getStrTo().split(";"));
		
    	if (!mail.getStrCc().equals("")) mail.setCcAddr(mail.getStrCc().split(";"));
    	
    	Map<String, Object> resData = new HashMap<>();
    	

    		int result = mailMapper.insertMail(mail);
    		
    		String[] summernoteImageNames = multipartRequest.getParameterValues("summernoteImageNames");
    		List<MultipartFile> files = multipartRequest.getFiles("attachs");
    		
    		int attachResult;
    		List<MailAtchDTO> atch = new ArrayList<>();
    		String[] fileNos = multipartRequest.getParameterValues("fileNo");
    		
    		if(fileNos !=  null) {
    			for(int i = 0; i < fileNos.length; i++) {
    				MailAtchDTO mailAtach = mailMapper.selectMailAttachByFileNo(Integer.parseInt(fileNos[i]));
    				mailAtach.setMailNo(mail.getMailNo());
    				mailMapper.insertMailAttachByFileNo(mailAtach);
    			}
    		}
    		
    		for(MultipartFile multipartFile : files) {
    			
    			try {
    				
    				if(result > 0) {
    					
    					if(summernoteImageNames !=  null) {
    						for(String filesystem : summernoteImageNames) {
    							MailSummerImageDTO summernoteImage = MailSummerImageDTO.builder()
    									.mailNo(mail.getMailNo())
    									.filesystem(filesystem)
    									.build();
    							mailMapper.insertMailSummerImage(summernoteImage);
    						}
    					}
    					
    				
	    				// ????????? ????????? ??????
	    				if(multipartFile != null && multipartFile.isEmpty() == false) {  // ??? ??? ?????????
	    					
	    					
    						if(files.get(0).getSize() == 0) {  // ????????? ?????? ?????? (files ???????????? [MultipartFile[field="files", filename=, contentType=application/octet-stream, size=0]] ????????? ???????????? ????????? files.size()??? 1??????.
    							attachResult = files.size();
    						} else {
    							attachResult = 0;
    						}
	    					
	    					// ?????? ??????
	    					String originName = multipartFile.getOriginalFilename();
	    					// IE??? origin??? ?????? ????????? ????????? ???????????? ???????????? ???
	    					originName = originName.substring(originName.lastIndexOf("\\") + 1);
	    					
	    					// ????????? ??????
	    					String changeName = myFileUtil.getFilename(originName);
	    					
	    					// ????????? ??????
	    					String mailPath = myFileUtil.getTodayPath();
	    					
	    					// ????????? ?????? ?????????
	    					File dir = new File(mailPath);
	    					if(dir.exists() == false) {
	    						dir.mkdirs();
	    					}
	    					
	    					// ????????? File ??????
	    					File file = new File(dir, changeName);
	    					
	    					// ???????????? ????????? ??????(????????? ??????)
	    					multipartFile.transferTo(file);
	
	    					// AttachDTO ??????
	    					MailAtchDTO attach = MailAtchDTO.builder()
	    							.mailNo(mail.getMailNo())
	    							.originName(originName)
	    							.changeName(changeName)
	    							.mailPath(mailPath)
	    							.build();
	    					
	    					String contentType = Files.probeContentType(file.toPath());  // ???????????? Content-Type(image/jpeg, image/png, image/gif)

	    					// ??????????????? ??????????????? ???????????? ??????
	    					if(contentType != null && contentType.startsWith("image")) {
	    					
	    						// ???????????? ????????? ??????
	    						Thumbnails.of(file)
	    							.size(50, 50)
	    							.toFile(new File(dir, "s_" + changeName));  // ???????????? ????????? s_??? ?????????
	    						
	    						// ???????????? ?????? ????????? ?????? ??????
	    						attach.setHasThumbnail(1);
	    					
	    					}
	    					
	    					// DB??? AttachDTO ??????
	    					attachResult += mailMapper.insertMailAttach(attach);
	    					atch.add(attach);
    					}
    				}
    				
    			} catch(Exception e) {
    				e.printStackTrace();
    			}
    			
    		}  // for
    		
    		try {
    		
    		String[] toAddrs = mail.getToAddr();
    		String[] toCcs = mail.getCcAddr();
    		
    		Map<String, Object> map = new HashMap<String, Object>();
    		int receiveEmp = 0;
    		
    		for(int i = 0; i < toAddrs.length; i++) {
    			String mailReceiver = toAddrs[i].substring(0, toAddrs[i].indexOf("@"));
    			
    			int cnt = 0;
    			
    			List<EmployeesDTO> empList = mailMapper.selectAllEmpList();
    			for(EmployeesDTO emp : empList) {
    				if(mailReceiver.equals(emp.getEmpNo() + "")) {
    					cnt += 1;
    				}
    				
    			}
    			if(cnt > 0) {
    				receiveEmp = Integer.parseInt(mailReceiver);
    				map.put("empNo", receiveEmp);
    				map.put("mailNo", mail.getMailNo());
    				
    				if(empMapper.selectEmpByMap(map) != null) {
    					map.put("receiveType", "To");
    					map.put("mailNo", mail.getMailNo());
    					mailMapper.insertReceivers(map);
    				}
    				receiveEmp = 0;
    				map.clear();
    			}
            }
    		
    		if(toCcs != null) {
    			for(int i = 0; i < toCcs.length; i++) {
        			receiveEmp = Integer.parseInt(toCcs[i].substring(0, toCcs[i].indexOf("@")));
        			map.put("empNo", receiveEmp);
        			map.put("mailNo", mail.getMailNo());
        			
        			if(empMapper.selectEmpByMap(map) != null) {
        				map.put("receiveType", "cc");
        				mailMapper.insertReceivers(map);
        			}
        			receiveEmp = 0;
        			map.clear();

                }
    		}
    		
    		map.put("empNo", empNo);
    		map.put("receiveType", "send");
    		map.put("mailNo", mail.getMailNo());
    		mailMapper.insertReceivers(map);
    		
    		String password = ((EmpAddrDTO)multipartRequest.getSession().getAttribute("mailUser")).getPassword();	// ??????
    		mailUser.setPassword(password);
    		
    		boolean sendResult = mailUtil.sendMail(mailUser, mail, summernoteImageNames, atch);
    		
    		if(sendResult) {
    			resData.put("status", HttpURLConnection.HTTP_OK);
    		} else {
    			resData.put("status", HttpURLConnection.HTTP_INTERNAL_ERROR);
    		}
    		
    		return resData;
    		
    	} catch(Exception e) {
    		e.printStackTrace();
    		resData.put("status", HttpURLConnection.HTTP_INTERNAL_ERROR);
    		return resData;
    	}
    	
	}
	
	@ResponseBody
	@Override
	public Map<String, Object> getReceiveMailList(HttpServletRequest request, String deleteCheck, String receiveType) {
		Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt.orElse("1"));
		
		Optional<String> record = Optional.ofNullable(request.getParameter("recordPerPage"));
		int recordPerPage = Integer.parseInt(record.orElse("20"));
		
		int empNo = ((EmpAddrDTO)request.getSession().getAttribute("mailUser")).getEmpNo();
		
		Map<String, Object> map = new HashMap<>();
		map.put("empNo", empNo);
		map.put("deleteCheck", deleteCheck);
		
		if(receiveType.equals("trash")) {
			map.put("trash", "true");
		}
		
		int totalRecord = mailMapper.selectReceiveMailCount(map);
		if(receiveType.equals("send")) {
			totalRecord = mailMapper.selectSendMailCount(map);
		}
		
		pageUtil.setPageUtil(page, totalRecord, recordPerPage);
		map.put("begin", pageUtil.getBegin() - 1);
		map.put("recordPerPage", pageUtil.getRecordPerPage());
		
		List<MailDTO> mailList = mailMapper.selectReceiveMailList(map);
		int nReadCnt = mailMapper.selectReadNotReceiveCount(map);
		if(receiveType.equals("send")) {
			mailList = mailMapper.selectSendMailList(map);
			nReadCnt = mailMapper.selectReadNotSendCount(empNo);
		}
		
		for (MailDTO mailInfo : mailList) {
			
			if((receiveType.equals("trash") && mailInfo.getReceiveType().equals("send")) || receiveType.equals("send")){
				
					int[] to = mailMapper.selectReceiveEmp(mailInfo.getMailNo());
					
					if(to.length > 0) {
						mailInfo.setEmpNo(to[0]);
					}
			}
			
			EmpAddrDTO addr = addrMapper.selectEmpAddrByNo(mailInfo.getEmpNo());
			
			if(addr.getName() != null) {
				mailInfo.setEmpName(addr.getName());
			}
			
			Date sendDate = mailInfo.getSendDate();
			SimpleDateFormat dateFormat;
			
			switch(mailUtil.checkDateFormat(sendDate)) {
			case "overYear" : dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
							  mailInfo.setReceiveDate(dateFormat.format(sendDate));
							  break;
			case "overDay"  : dateFormat = new SimpleDateFormat("MM.dd HH:mm");
							  mailInfo.setReceiveDate(dateFormat.format(sendDate));
							  break;
			case "inToday"  : dateFormat = new SimpleDateFormat("a hh:mm");
							  mailInfo.setReceiveDate(dateFormat.format(sendDate));
			  				  break;
			default : break;
			}
			
        }
		
		Map<String, Object> result = new HashMap<>();
		
		result.put("mailList", mailList);
		result.put("totalRecord", totalRecord);
		result.put("nReadCnt", nReadCnt);
		result.put("pageUtil", pageUtil);
		
		return result;
	}
	
	@Override
	public Map<String, Object> getReceiveMailInfo(HttpServletRequest request, Model model, ReceiversDTO receivData) {
		
		Optional<String> opt = Optional.ofNullable(request.getParameter("mailNo"));
		int mailNo = Integer.parseInt(opt.orElse("0"));
		int empNo = ((EmpAddrDTO)request.getSession().getAttribute("mailUser")).getEmpNo();
		
		Map<String, Object> map = new HashMap<>();
		map.put("empNo", empNo);
		map.put("mailNo", mailNo);
		map.put("deleteCheck", receivData.getDeleteCheck());
		
		String readCheck = null;
		
		if(receivData.getReceiveType().equals("send")) {
			readCheck = mailMapper.selectSendReceiverByMap(map).getReadCheck();
		} else {
			readCheck = mailMapper.selectReceiverByMap(map).getReadCheck();
			// ??? ?????? ??????
		}
		
		if(readCheck != null && readCheck.equals("N")) {
			map.put("checkType", "READ_CHECK");
			map.put("check", "Y");
			mailMapper.updateCheckByMap(map);
		}
		
		MailDTO mail = mailMapper.selectMailByMap(map);
		mail.setEmpName(addrMapper.selectEmpAddrByNo(mail.getEmpNo()).getName());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy??? MM??? dd??? (E) a hh:mm", Locale.KOREAN);
		mail.setReceiveDate(dateFormat.format(mail.getSendDate()));
		mail.setReadCheck("Y");
		
		List<ReceiversDTO> receiverList = mailMapper.selectReceiverList(mailNo);
		
		List<EmpAddrDTO> addrList = new ArrayList<>();
		for (ReceiversDTO receiver : receiverList) {
			
			EmpAddrDTO addr = addrMapper.selectEmpAddrByNo(receiver.getEmpNo());
			addr.setReceiveType(receiver.getReceiveType());
			
			addrList.add(addr);
		}
		
		int totalRecord = mailMapper.selectReceiveMailCount(map);
		
		String in = request.getParameter("in");
		if(in != null && in.equals("trash")) {
			map.put("trash", "true");
		}
		int nReadCnt = mailMapper.selectReadNotReceiveCount(map);
		if(receivData.getReceiveType().equals("send")) {
			nReadCnt = mailMapper.selectReadNotSendCount(empNo);
		}
		
		model.addAttribute("mail", mail);
		model.addAttribute("addrList", addrList);
		model.addAttribute("nReadCnt", nReadCnt);
		model.addAttribute("totalRecord", totalRecord);
		model.addAttribute("receivData", receivData);
		
		Map<String, Object> mailInfo = new HashMap<>();
		mailInfo.put("mail", mail);
		mailInfo.put("addrList", addrList);
		mailInfo.put("nReadCnt", nReadCnt);
		mailInfo.put("totalRecord", totalRecord);
		
		List<MailAtchDTO> attachList = mailMapper.selectMailAttachList(mailNo);
		if(attachList != null) {
			model.addAttribute("attachList", attachList);
			model.addAttribute("attachCnt", attachList.size());
			mailInfo.put("attachList", attachList);
		}
		
		return mailInfo;
		
	}
	
	@Override
	public Map<String, Object> changeRead(List<String> mailNo, List<String> readCheck, HttpServletRequest request) {
		
		int empNo = ((EmpAddrDTO)request.getSession().getAttribute("mailUser")).getEmpNo();
		
		Map<String, Object> map = new HashMap<>();
		
		int updateResult = 0;
		
		for(int i = 0; i < mailNo.size(); i++) {
			map.put("empNo", empNo);
			map.put("checkType", "READ_CHECK");
			
			map.put("mailNo", mailNo.get(i));
			
			if(readCheck.get(i).equals("N")) {
				map.put("check", "Y");
			} else if(readCheck.get(i).equals("Y")) {
				map.put("check", "N");
			}
			
			updateResult += mailMapper.updateCheckByMap(map);
			map.clear();
		}
		
		Map<String, Object> result = new HashMap<>();
		
		if(updateResult == mailNo.size()) {
			result.put("isResult", true);
		} else {
			result.put("isResult", false);
		}
		
		return result;
	}
	
	@Override
	public Map<String, Object> moveInTrash(List<String> mailNo, String receiveType, HttpServletRequest request) {
		
		int empNo = ((EmpAddrDTO)request.getSession().getAttribute("mailUser")).getEmpNo();
		
		Map<String, Object> map = new HashMap<>();
		
		int updateResult = 0;
		
		for(int i = 0; i < mailNo.size(); i++) {
			
			map.put("empNo", empNo);
			map.put("checkType", "DELETE_CHECK");
			map.put("mailNo", mailNo.get(i));
			
			String deleteCheck = null;
			
			if(receiveType.equals("send")) {
				deleteCheck = mailMapper.selectSendReceiverByMap(map).getDeleteCheck();
			} else if(receiveType.equals("ToCc")) {
				deleteCheck = mailMapper.selectReceiverByMap(map).getDeleteCheck();
			}
			
			if(deleteCheck.equals("N")) {
				map.put("check", "Y");
				updateResult += mailMapper.updateCheckByMap(map);
			}
			
			map.clear();

		}
		
		Map<String, Object> result = new HashMap<>();
		
		if(updateResult == mailNo.size()) {
			result.put("isDelete", true);
		} else {
			result.put("isDelete", false);
		}
		return result;
	}
	
	@Override
	public Map<String, Object> saveSummernoteImage(MultipartHttpServletRequest multipartRequest) {
		
		MultipartFile multipartFile = multipartRequest.getFile("file");
			
		String path = myFileUtil.getSummernotePath();
				
		String filesystem = myFileUtil.getFilename(multipartFile.getOriginalFilename());
		
		File dir = new File(path);
		if(dir.exists() == false) {
			dir.mkdirs();
		}
		
		File file = new File(path, filesystem);
		
		try {
			multipartFile.transferTo(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("src", multipartRequest.getContextPath() + "/load/image/" + filesystem);  // ????????? mapping?????? ??????
		map.put("filesystem", filesystem); 
		return map;
		
	}
	
	@Override
	public ResponseEntity<Resource> download(String userAgent, int fileNo) {
		
		// ???????????? ??? ?????? ????????? ??????(??????, ??????)
		MailAtchDTO attach = mailMapper.selectMailAttachByNo(fileNo);
		File file = new File(attach.getMailPath(), attach.getChangeName());
		
		// ????????? Resource
		Resource resource = new FileSystemResource(file);
		
		// Resource??? ????????? ?????? (??????????????? ????????? ??????)
		if(resource.exists() == false) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		}
		
		// ???????????? ?????? ?????????(???????????? ?????? ????????? ??????)
		String origin = attach.getOriginName();
		try {
			
			// IE (userAgent??? "Trident"??? ???????????? ??????)
			if(userAgent.contains("Trident")) {
				origin = URLEncoder.encode(origin, "UTF-8").replaceAll("\\+", " ");
			}
			// Edge (userAgent??? "Edg"??? ???????????? ??????)
			else if(userAgent.contains("Edg")) {
				origin = URLEncoder.encode(origin, "UTF-8");
			}
			// ?????????
			else {
				origin = new String(origin.getBytes("UTF-8"), "ISO-8859-1");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// ???????????? ?????? ?????????
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Disposition", "attachment; filename=" + origin);
		header.add("Content-Length", file.length() + "");
		
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
		
	}
	
	@Override
	public ResponseEntity<Resource> downloadAll(String userAgent, int mailNo) {
		
		// /storage/temp ??????????????? ?????? zip ????????? ?????? ??? ?????? ???????????? ?????? ??? ??????
		// com.gdu.app14.batch.DeleteTempFiles??? ????????? /storage/temp ??????????????? ?????? zip ????????? ??????????????? ?????????
		
		// ???????????? ??? ?????? ???????????? ??????(??????, ??????)
		List<MailAtchDTO> attachList = mailMapper.selectMailAttachList(mailNo);
		
		// zip ????????? ????????? ?????? ?????????
		FileOutputStream fout = null;
		ZipOutputStream zout = null;   // zip ?????? ?????? ?????????
		FileInputStream fin = null;
		
		// /storage/temp ??????????????? zip ?????? ??????
		String tempPath = myFileUtil.getTempPath();
		
		File tempDir = new File(tempPath);
		if(tempDir.exists() == false) {
			tempDir.mkdirs();
		}
		
		// zip ???????????? ??????????????? ????????? ??????
		String tempName =  System.currentTimeMillis() + ".zip";
		
		try {
			
			fout = new FileOutputStream(new File(tempDir, tempName));
			zout = new ZipOutputStream(fout);
			
			// ????????? ????????? ??????
			if(attachList != null && attachList.isEmpty() == false) {

				// ?????? ?????? ????????? ??????
				for(MailAtchDTO attach : attachList) {
					
					// zip ????????? ?????? ?????? ??????
					ZipEntry zipEntry = new ZipEntry(attach.getOriginName());
					zout.putNextEntry(zipEntry);
					
					fin = new FileInputStream(new File(attach.getMailPath(), attach.getChangeName()));
					byte[] buffer = new byte[1024];
					int length;
					while((length = fin.read(buffer)) != -1){
						zout.write(buffer, 0, length);
					}
					zout.closeEntry();
					fin.close();
					
				}
				
				zout.close();

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		// ????????? Resource
		File file = new File(tempDir, tempName);
		Resource resource = new FileSystemResource(file);
		
		// Resource??? ????????? ?????? (??????????????? ????????? ??????)
		if(resource.exists() == false) {
			return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
		}
		
		// ???????????? ?????? ?????????
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Disposition", "attachment; filename=" + tempName);  // ??????????????? zip???????????? ?????????????????? ?????? ????????? ????????? ??????
		header.add("Content-Length", file.length() + "");
		
		return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
		
	}
	
	@Override
	public List<MailAtchDTO> getMailAttach(int mailNo) {
		return mailMapper.selectMailAttachList(mailNo);
	}
	
	@Override
	public Map<String, Object> deleteReceiverData(List<String> mailNo, List<String> receiveType, HttpServletRequest request) {
		
		int empNo = ((EmpAddrDTO)request.getSession().getAttribute("mailUser")).getEmpNo();
		
		Map<String, Object> map = new HashMap<>();
		
		int updateResult = 0;
		
		for(int i = 0; i < mailNo.size(); i++) {
			
			map.put("empNo", empNo);
			map.put("mailNo", mailNo.get(i));
			map.put("receiveType", receiveType.get(i));
			
			updateResult += mailMapper.deleteReceiver(map);
			
			map.clear();

		}
		
		Map<String, Object> result = new HashMap<>();
		
		if(updateResult == mailNo.size()) {
			result.put("isDelete", true);
		} else {
			result.put("isDelete", false);
		}
		return result;
	}
	
	@Override
	public Map<String, Object> restoreReceiverData(List<String> mailNo, List<String> receiveType, HttpServletRequest request) {
		int empNo = ((EmpAddrDTO)request.getSession().getAttribute("mailUser")).getEmpNo();
		
		Map<String, Object> map = new HashMap<>();
		
		int updateResult = 0;
		
		for(int i = 0; i < mailNo.size(); i++) {
			
			map.put("empNo", empNo);
			map.put("checkType", "DELETE_CHECK");
			map.put("mailNo", mailNo.get(i));
			
			String deleteCheck = null;
			
			if(receiveType.get(i).equals("send")) {
				deleteCheck = mailMapper.selectSendReceiverByMap(map).getDeleteCheck();
			} else if(receiveType.get(i).equals("To") || receiveType.get(i).equals("cc")) {
				deleteCheck = mailMapper.selectReceiverByMap(map).getDeleteCheck();
			}
			
			if(deleteCheck.equals("Y")) {
				map.put("check", "N");
				updateResult += mailMapper.updateCheckByMap(map);
			}
			
			map.clear();

		}
		
		Map<String, Object> result = new HashMap<>();
		
		if(updateResult == mailNo.size()) {
			result.put("isRestore", true);
		} else {
			result.put("isRestore", false);
		}
		return result;
	}
	
}
