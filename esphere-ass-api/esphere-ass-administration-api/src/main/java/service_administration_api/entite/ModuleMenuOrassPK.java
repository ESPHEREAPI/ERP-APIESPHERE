/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

/**
 *
 * @author USER01
 */
@Embeddable
public class ModuleMenuOrassPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "CODEMENU")
    private String codemenu;
    @Basic(optional = false)
    @Column(name = "CODEMODU")
    private String codemodu;
    @Basic(optional = false)
    @Column(name = "CODELANG")
    private String codelang;

    public ModuleMenuOrassPK() {
    }

    public ModuleMenuOrassPK(String codemenu, String codemodu, String codelang) {
        this.codemenu = codemenu;
        this.codemodu = codemodu;
        this.codelang = codelang;
    }

    public String getCodemenu() {
        return codemenu;
    }

    public void setCodemenu(String codemenu) {
        this.codemenu = codemenu;
    }

    public String getCodemodu() {
        return codemodu;
    }

    public void setCodemodu(String codemodu) {
        this.codemodu = codemodu;
    }

    public String getCodelang() {
        return codelang;
    }

    public void setCodelang(String codelang) {
        this.codelang = codelang;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codemenu != null ? codemenu.hashCode() : 0);
        hash += (codemodu != null ? codemodu.hashCode() : 0);
        hash += (codelang != null ? codelang.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModuleMenuOrassPK)) {
            return false;
        }
        ModuleMenuOrassPK other = (ModuleMenuOrassPK) object;
        if ((this.codemenu == null && other.codemenu != null) || (this.codemenu != null && !this.codemenu.equals(other.codemenu))) {
            return false;
        }
        if ((this.codemodu == null && other.codemodu != null) || (this.codemodu != null && !this.codemodu.equals(other.codemodu))) {
            return false;
        }
        if ((this.codelang == null && other.codelang != null) || (this.codelang != null && !this.codelang.equals(other.codelang))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.ModuleMenuOrassPK[ codemenu=" + codemenu + ", codemodu=" + codemodu + ", codelang=" + codelang + " ]";
    }
    
}
