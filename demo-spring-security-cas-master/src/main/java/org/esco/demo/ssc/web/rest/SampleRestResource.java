package org.esco.demo.ssc.web.rest;

import lombok.extern.slf4j.Slf4j;

import org.esco.demo.ssc.DayInfo;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequestMapping("/api")
public class SampleRestResource {

	@RequestMapping(value = "/now", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public  ResponseEntity<DayInfo> getDayInfo() {
		
		
		//return "Time from API Service is " + DateTime.now().toString();
		DayInfo dayInfo = new DayInfo();
		dayInfo.setCurDate(DateTime.now().toLocalDate().toString());
		dayInfo.setCurTime(DateTime.now().toLocalTime().toString());				
		return new ResponseEntity<DayInfo>(dayInfo,  HttpStatus.OK);
		
		
	}

}
