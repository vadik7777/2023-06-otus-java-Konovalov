package ru.otus.hw14.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.hw14.model.Address;
import ru.otus.hw14.model.Client;
import ru.otus.hw14.model.Phone;

public class ClientResultSetExtractorClass implements ResultSetExtractor<List<Client>> {

    @SuppressWarnings("java:S2259")
    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var clientList = new ArrayList<Client>();
        Long clientId = 0L;
        Client client;
        Set<Phone> phoneSet = null;
        while (rs.next()) {
            var nextClientId = rs.getLong("id");
            if (Objects.equals(clientId, nextClientId)) {
                var phoneId = rs.getLong("phone_id");
                if (!Objects.equals(0L, phoneId)) {
                    var number = rs.getString("phone_number");
                    phoneSet.add(new Phone(phoneId, number));
                }
            } else {
                clientId = rs.getLong("id");
                var clientName = rs.getString("name");
                client = new Client(clientId, clientName);
                var addressId = rs.getLong("address_id");
                if (!Objects.equals(0L, addressId)) {
                    var addressStreet = rs.getString("address_street");
                    var address = new Address(addressId, addressStreet);
                    client.setAddress(address);
                }
                phoneSet = new HashSet<>();
                client.setPhones(phoneSet);
                var phoneId = rs.getLong("phone_id");
                if (!Objects.equals(0L, phoneId)) {
                    var phoneNumber = rs.getString("phone_number");
                    var phone = new Phone(phoneId, phoneNumber);
                    phoneSet.add(phone);
                }
                clientList.add(client);
            }
        }
        return clientList;
    }
}
