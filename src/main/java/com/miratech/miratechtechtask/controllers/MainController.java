package com.miratech.miratechtechtask.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Controller class responsible for handling requests related to the main page redirection.
 */
@RestController
@Log4j2
@RequiredArgsConstructor
public class MainController {

    /**
     * Path to the main page.
     */
    public static final String PATH = "miratech/tasks";

    /**
     * Redirects requests to the main page.
     *
     * @param request  HttpServletRequest object representing the incoming request.
     * @param response HttpServletResponse object representing the response to be sent back to the client.
     * @throws IOException if an I/O error occurs while sending the redirect response.
     */
    @ApiOperation("Main page redirection to resource")
    @ApiResponses({
            @ApiResponse(code = 302, message = "Return all present tasks")
    })
    @GetMapping
    public void redirect(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getRequestURI() + PATH);
    }

}
