package covid.models;

import java.io.Serializable;

/**
 * Classe que representa um pais
 * @author Markson Arguello
 * 
 */
public class Pais implements Serializable {
    public static final long serialVersionUID = 2000L;

    private String nome;
    private String codigo;
    private String slug;
    private float latitude;
    private float longitude;
    
    /**
     * Construtor da classe Pais
     * 
     * @param nome nome do pa�s
     * @param codigo c�digo do pa�s
     * @param slug slug do pa�s
     * @param latitude latitude do pa�s
     * @param longitude longitude do pa�s
     */

    public Pais(String nome, String codigo, String slug, float latitude, float longitude) {
        this.nome = nome;
        this.codigo = codigo;
        this.slug = slug;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    /**
     * 
     * @return nome do pa�s
     */
    public String getNome() {
        return nome;
    }

    /**
     * 
     * @return c�digo do pa�s
     */
    public String getCodigo() {
        return codigo;
    }
    /**
     * 
     * @return slug do pa�s
     */
    public String getSlug() {
        return slug;
    }
    /**
     * 
     * @return latitude do pa�s
     */
    public float getLatitude() {
        return latitude;
    }
    /**
     * 
     * @return longitude do pa�s
     */
    public float getLongitude() {
        return longitude;
    }
}
