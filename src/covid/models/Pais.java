package covid.models;

import java.io.Serializable;

/**
 * @author Markson Arguello - marksonva@dcc.ufrj.br
 */
public class Pais implements Serializable {
    public static final long serialVersionUID = 2000L;

    private String nome;
    private String codigo;
    private String slug;
    private float latitude;
    private float longitude;

    public Pais(String nome, String codigo, String slug, float latitude, float longitude) {
        this.nome = nome;
        this.codigo = codigo;
        this.slug = slug;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getSlug() {
        return slug;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }
}
