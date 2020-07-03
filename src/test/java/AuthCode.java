import lombok.Data;
import java.sql.Date;

@Data
public class AuthCode {
    private String id;
    private String user_id;
    private String code;
    private Date created;
}
