package com.experiment.www.codingtask.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.experiment.www.codingtask.service.EnrichTradeService;

@RestController
@RequestMapping("/api/v1")
public class EnrichController {
    private static final Logger logger = LoggerFactory.getLogger(EnrichController.class);

    @Autowired
    EnrichTradeService enrichTradeService;

    public EnrichController(EnrichTradeService enrichTradeService) {
        this.enrichTradeService = enrichTradeService;
    }

    @PostMapping(value = "/enrich", produces = "text/csv", consumes = {"text/csv"})
    public ResponseEntity<StreamingResponseBody> ReceiveFileUpload(@RequestBody String file, final HttpServletResponse response) {
        response.setContentType("text/csv");
        StreamingResponseBody stream = out -> {
            try (out){
                enrichTradeService.Enrich(file.getBytes(), out);
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                out.close();
            }
        };

        return ResponseEntity.ok(stream);

    }
}
