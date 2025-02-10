package es.project.apiagencia.models;
import lombok.Data;


@Data
public class PasswordUpdateDTO {

    private String passwordNew;
    private String passwordOld;
}