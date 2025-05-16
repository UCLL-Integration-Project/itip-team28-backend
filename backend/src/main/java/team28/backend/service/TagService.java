package team28.backend.service;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import team28.backend.model.Tag;
import team28.backend.repository.TagRepository;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final InfluxDBClient influxDBClient;

    public TagService(TagRepository tagRepository, InfluxDBClient influxDBClient) {
        this.tagRepository = tagRepository;
        this.influxDBClient = influxDBClient;
    }

    public List<Tag> GetAllTags() {
        return tagRepository.findAll();
    }

    public List<Map<String, String>> getTagDataFromInflux() {
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
