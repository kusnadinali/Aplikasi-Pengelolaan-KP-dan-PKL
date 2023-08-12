package com.jtk.ps.api.controller;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jtk.ps.api.dto.EtproAspectDto;
import com.jtk.ps.api.dto.EtproUpdateValuesDto;
import com.jtk.ps.api.service.Interface.IEtproService;
import com.jtk.ps.api.util.Constant;
import com.jtk.ps.api.util.ResponseHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Controller
@RequestMapping("/etpro")
public class EtproController {
    
    @Autowired
    private IEtproService etproService;

    @GetMapping("/aspects")
    public ResponseEntity<Object> getAllAspect(HttpServletRequest request){
        Integer prodiId = (Integer) Objects.requireNonNull(request.getAttribute(Constant.VerifyConstant.ID_PRODI));
        return ResponseHandler.generateResponse("Get all Etika Profesi Aspect succeed",HttpStatus.OK, etproService.getAllAspect(prodiId));
    }

    @PostMapping("/aspects")
    public ResponseEntity<Object> createAspect(@RequestBody EtproAspectDto request, HttpServletRequest httpRequest){
        Integer prodiId = (Integer) Objects.requireNonNull(httpRequest.getAttribute(Constant.VerifyConstant.ID_PRODI));
        request.setProdiId(prodiId);
        etproService.createAspect(request);
        return ResponseHandler.generateResponse("Create Aspect Succeed",HttpStatus.OK);
    }

    @PutMapping("/aspects")
    public ResponseEntity<Object> updateAspect(@RequestBody List<EtproAspectDto> updateAspect){
        etproService.updateAspect(updateAspect);
        return ResponseHandler.generateResponse("Update Aspect Succeed",HttpStatus.OK);
    }

    @GetMapping("/forms")
    public ResponseEntity<Object> getAllForm(HttpServletRequest request){
        Integer prodiId = (Integer) Objects.requireNonNull(request.getAttribute(Constant.VerifyConstant.ID_PRODI));
        return ResponseHandler.generateResponse("Get all Etika Profesi Teori Form succeed",HttpStatus.OK, etproService.getAllForm(prodiId));
    }

    @PutMapping("/forms/update")
    public ResponseEntity<Object> updateFormAndValues(@RequestBody EtproUpdateValuesDto request){
        etproService.updateFormAndValues(request);
        return ResponseHandler.generateResponse("Update Form And Values Succeed",HttpStatus.OK);
    }

    @GetMapping("/recapitulations")
    public ResponseEntity<Object> getRecapitulation(HttpServletRequest request){
        Integer prodiId = (Integer) Objects.requireNonNull(request.getAttribute(Constant.VerifyConstant.ID_PRODI));
        return ResponseHandler.generateResponse("Get Recapitulation Etika Profesi Teori succeed",HttpStatus.OK, etproService.getRecapitulation(prodiId));
    }

    @GetMapping("/generate-seminar")
    public ResponseEntity<Resource> getXLS(HttpServletRequest request) {
        Integer prodiId = (Integer) Objects.requireNonNull(request.getAttribute(Constant.VerifyConstant.ID_PRODI));
        String filename;
        if(prodiId == 0){
            filename = "rekapitulasi etika profesi Prodi D3.xlsx";
        }else{
            filename = "rekapitulasi etika profesi Prodi D4.xlsx";
        }
        
        InputStreamResource file = new InputStreamResource(etproService.loadEtpro(prodiId));
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(file);
    }
}
