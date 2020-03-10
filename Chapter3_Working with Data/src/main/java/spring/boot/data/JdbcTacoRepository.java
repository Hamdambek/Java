package spring.boot.data;

import lombok.Data;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import spring.boot.Ingredient;
import spring.boot.Taco;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

/**
 * @Created 09 / 03 / 2020 - 5:20 PM
 * @project SpringData
 * @Author Hamdamboy
 */
@Repository
public class JdbcTacoRepository implements TacoRepository{
    private final JdbcTemplate jdbc;

    //
    private JdbcTacoRepository(JdbcTemplate jdbc){
        this.jdbc = jdbc;
    }

    @Override
    public Taco save(Taco taco) {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        for(Ingredient ingredient: taco.getIngredients()) {
            saveIngredientToTaco(ingredient, tacoId);
        }
        return taco;
    }

    private long saveTacoInfo(Taco taco) {
        taco.setCreateAt(new Data());
        PreparedStatementCreator psc = new PreparedStatementCreatorFactory("insert into Taco(name, createAt) value(?, ?)");
        Types.VARCHAR, Types.TIMESTAMP).newPreparedStatementCreator(Arrays.asList(taco.getName(), new Timestamp(taco.getCreateAt().getTime())));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);

        return keyHolder.getKey().longValue();
    }

    private void saveIngredientToTaco(
            Ingredient ingredient, long tacoId ) {
        jdbc.update("insert into Taco_Ingredients (taco, ingredient)" + "values ( ?, ? )", tacoId, ingredient.getId());
    }
}
