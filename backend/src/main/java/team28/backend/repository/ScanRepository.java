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
                String carId = (String) record.getValues().get("car_id");
                String readerId = (String) record.getValues().get("reader_id");

                System.out.println("carId: " + carId + ", readerId: " + readerId);

                Car car = CarRepository.findByName(carId);
                Reader reader = ReaderRepository.findByMacAddress(readerId);

                System.out.println("Car: " + car + ", Reader: " + reader);

                LocalDateTime timestamp = record.getTime().atOffset(ZoneOffset.UTC).toLocalDateTime();
                System.out.println("Timestamp: " + timestamp);

                Scan scan = new Scan(car, reader, timestamp);
                System.out.println("Scan created: " + scan);

                scans.add(scan);
            }
        }

        return scans;
    }

    public Scan findLatest() {
        String flux = String.format("""
                    from(bucket: "%s")
                    |> range(start: -30d)
                    |> filter(fn: (r) => r._measurement == "halt")
                    |> sort(columns: ["_time"], desc: true)
                    |> pivot(rowKey: ["_time"], columnKey: ["_field"], valueColumn: "_value")
                    |> limit(n: 1)
                """, bucket);

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(flux, org);

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                String carId = (String) record.getValues().get("car_id");
                String readerId = (String) record.getValues().get("reader_id");

                Car car = CarRepository.findByName(carId);
                Reader reader = ReaderRepository.findByMacAddress(readerId);

                LocalDateTime timestamp = record.getTime().atOffset(ZoneOffset.UTC).toLocalDateTime();

                return new Scan(car, reader, timestamp);
            }
        }

        return null;
    }

}
