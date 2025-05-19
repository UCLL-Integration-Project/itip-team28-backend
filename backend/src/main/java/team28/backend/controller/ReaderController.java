package team28.backend.controller;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import team28.backend.model.Reader;
import team28.backend.service.ReaderService;

import java.util.*;

@RestController
@RequestMapping("/readers")
public class ReaderController {

    private final ReaderService ReaderService;
    private final InfluxDBClient influxDBClient;

    public ReaderController(ReaderService ReaderService, InfluxDBClient influxDBClient) {
        this.ReaderService = ReaderService;
        this.influxDBClient = influxDBClient;
    }

    @Operation(summary = "Get all tags")
    @ApiResponse(responseCode = "200", description = "List of readers returned successfully")
    @GetMapping
    public List<Reader> getAllReaders() {
        return ReaderService.GetAllReaders();
    }

    @Operation(summary = "Get all data")
    @ApiResponse(responseCode = "200", description = "List of data returned successfully")
    @GetMapping("/data")
    public List<Map<String, String>> getReaderData() {
        QueryApi queryApi = influxDBClient.getQueryApi();

        String fluxQuery = "from(bucket: \"Integration\")"
                + " |> range(start: -1h)"
                + " |> filter(fn: (r) => r._measurement == \"rfid\")";

        List<FluxTable> tables = queryApi.query(fluxQuery);
        List<Map<String, String>> result = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                Map<String, String> data = new HashMap<>();
                data.put("carId", String.valueOf(record.getValueByKey("car_id")));
                data.put("readerId", String.valueOf(record.getValueByKey("reader_id")));
                data.put("timestampRead", String.valueOf(record.getValueByKey("timestamp_read")));
                result.add(data);
            }
        }
        return result;
    }
}
