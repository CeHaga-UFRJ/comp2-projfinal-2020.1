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
     * @param nome nome do país
     * @param codigo código do país
     * @param slug slug do país
     * @param latitude latitude do país
     * @param longitude longitude do país
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
     * @return nome do país
     */
    public String getNome() {
        return nome;
    }

    /**
     * 
     * @return código do país
     */
    public String getCodigo() {
        return codigo;
    }
    /**
     * 
     * @return slug do país
     */
    public String getSlug() {
        return slug;
    }
    /**
     * 
     * @return latitude do país
     */
    public float getLatitude() {
        return latitude;
    }
    /**
     * 
     * @return longitude do país
     */
    public float getLongitude() {
        return longitude;
    }
}
