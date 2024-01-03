package ru.otus.hw14.repository;

import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import ru.otus.hw14.model.Client;

public interface ClientRepository extends ListCrudRepository<Client, Long> {

    @Override
    List<Client> findAll();

    @Query(
            value =
                    """
                   select c.id as id,
                   c.name as name,
                   a.id as address_id,
                   a.street as address_street,
                   p.id as phone_id,
                   p.number as phone_number
                   from client c
                   left outer join address a
                   on c.id = a.client_id
                   left outer join phone p
                   on c.id = p.client_id
                   order by c.id""",
            resultSetExtractorClass = ClientResultSetExtractorClass.class)
    List<Client> findAllFull();

    @Override
    <S extends Client> S save(S entity);
}
