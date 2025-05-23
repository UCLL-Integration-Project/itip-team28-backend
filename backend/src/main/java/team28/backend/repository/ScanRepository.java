package team28.backend.repository;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;

import team28.backend.model.Car;
import team28.backend.model.Scan;
import team28.backend.model.Reader;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class ScanRepository {

    private final InfluxDBClient influxDBClient;
    private final CarRepository CarRepository;
    private final ReaderRepository ReaderRepository;
    private final String bucket;
    private final String org;

    public ScanRepository(
            InfluxDBClient influxDBClient,
            CarRepository CarRepository,
            ReaderRepository ReaderRepository,
            @Qualifier("influxBucket") String bucket,
            @Qualifier("influxOrg") String org) {
        this.influxDBClient = influxDBClient;
        this.CarRepository = CarRepository;
        this.ReaderRepository = ReaderRepository;
        this.bucket = bucket;
        this.org = org;
    }

    public List<Scan> findAll() {

        String flux = String.format("""
                    from(bucket: "%s")
                    |> range(start: -30d)
                    |> filter(fn: (r) => r._measurement == "halt")
                    |> pivot(rowKey: ["_time"], columnKey: ["_field"], valueColumn: "_value")
                """, bucket);

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(flux, org);

        List<Scan> scans = new ArrayList<>();
        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {

                String carId = (String) record.getValueByKey("car_id");
                Car car = CarRepository.findByName(carId);

                String readerId = (String) record.getValueByKey("reader_id");
                Reader reader = ReaderRepository.findByMacAddress(readerId);

                LocalDateTime timestamp = record.getTime().atOffset(ZoneOffset.UTC).toLocalDateTime();
                Scan scan = new Scan(car, reader, timestamp);

                scans.add(scan);
            }
        }

        return scans;
    }
}
