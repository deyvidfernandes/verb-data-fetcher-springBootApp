package br.com.deyvidfernandes.verbDataFetcher.verb;

import br.com.deyvidfernandes.verbDataFetcher.database.DatabaseConnector;
import br.com.deyvidfernandes.verbDataFetcher.database.queries.InsertQuery;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

import static br.com.deyvidfernandes.verbDataFetcher.database.queries.Queries.getQueryDialect;


@RestController
@RequestMapping("/api/verb-data")
public class VerbDataController {

    @PostMapping("")
    public ResponseEntity<Object> persistVerb(@RequestBody VerbModel[] verbData) throws FailPersistingVerbsException {
        try {
            DatabaseConnector.openConnection();
            InsertQuery<VerbModel> insertVerbQuery = new InsertQuery<>(
                    "(infinitive, infinitive_audio, simple_past, simple_past_audio, simple_past_uk, simple_past_uk_audio, participle, participle_audio, participle_uk, participle_uk_audio, usageIndex, phonetics, definitions)",
                    DatabaseConnector.getTable(),
                    new VerbModel()
            );
            for (VerbModel verb : verbData) { insertVerbQuery.addValue(verb); }
            var dbType = DatabaseConnector.getDatabaseType();
            String query = getQueryDialect(dbType, insertVerbQuery);
            System.out.println(query);

            DatabaseConnector.update(query);

            DatabaseConnector.closeConnection();
        } catch (SQLException e) {
            throw new FailPersistingVerbsException(e.getMessage());
        }

        return  ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
