package team28.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import team28.backend.exceptions.ServiceException;
import team28.backend.model.Route;
import team28.backend.service.RouteService;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private final RouteService RouteService;

    public RouteController(RouteService RouteService) {
        this.RouteService = RouteService;
    }

    @Operation(summary = "Get all routes")
    @ApiResponse(responseCode = "200", description = "List of routes returned successfully")
    @GetMapping
    public List<Route> GetAllRoutes() {
        return RouteService.GetAllRoutes();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ServiceException.class })
    public String handleValidationExceptions(ServiceException ex) {
        return ex.getMessage();
    }
}
