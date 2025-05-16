package team28.backend.controller;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import team28.backend.model.Tag;
import team28.backend.service.TagService;

import java.util.*;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final InfluxDBClient influxDBClient;

    public TagController(TagService tagService, InfluxDBClient influxDBClient) {
        this.tagService = tagService;
        this.influxDBClient = influxDBClient;
    }

    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.GetAllTags();
    }

    @GetMapping("/data")
    public List<Map<String, String>> getTagData() {
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
