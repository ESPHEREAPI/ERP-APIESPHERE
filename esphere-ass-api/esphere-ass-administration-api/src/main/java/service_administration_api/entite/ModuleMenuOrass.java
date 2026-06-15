/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "MODULE_MENU_ORASS")
//@NamedQueries({
//    @NamedQuery(name = "ModuleMenuOrass.findAll", query = "SELECT m FROM ModuleMenuOrass m"),
//    @NamedQuery(name = "ModuleMenuOrass.findByCodemenu", query = "SELECT m FROM ModuleMenuOrass m WHERE m.moduleMenuOrassPK.codemenu = :codemenu"),
//    @NamedQuery(name = "ModuleMenuOrass.findByCodemodu", query = "SELECT m FROM ModuleMenuOrass m WHERE m.moduleMenuOrassPK.codemodu = :codemodu"),
//    @NamedQuery(name = "ModuleMenuOrass.findByDesimodu", query = "SELECT m FROM ModuleMenuOrass m WHERE m.desimodu = :desimodu"),
//    @NamedQuery(name = "ModuleMenuOrass.findByNomModu", query = "SELECT m FROM ModuleMenuOrass m WHERE m.nomModu = :nomModu"),
//    @NamedQuery(name = "ModuleMenuOrass.findByMenuasso", query = "SELECT m FROM ModuleMenuOrass m WHERE m.menuasso = :menuasso"),
//    @NamedQuery(name = "ModuleMenuOrass.findByNomRole", query = "SELECT m FROM ModuleMenuOrass m WHERE m.nomRole = :nomRole"),
//    @NamedQuery(name = "ModuleMenuOrass.findByNumeLot", query = "SELECT m FROM ModuleMenuOrass m WHERE m.numeLot = :numeLot"),
//    @NamedQuery(name = "ModuleMenuOrass.findByFlagvali", query = "SELECT m FROM ModuleMenuOrass m WHERE m.flagvali = :flagvali"),
//    @NamedQuery(name = "ModuleMenuOrass.findByFormappl", query = "SELECT m FROM ModuleMenuOrass m WHERE m.formappl = :formappl"),
//    @NamedQuery(name = "ModuleMenuOrass.findByOrdraffi", query = "SELECT m FROM ModuleMenuOrass m WHERE m.ordraffi = :ordraffi"),
//    @NamedQuery(name = "ModuleMenuOrass.findByNomFont", query = "SELECT m FROM ModuleMenuOrass m WHERE m.nomFont = :nomFont"),
//    @NamedQuery(name = "ModuleMenuOrass.findByTailfont", query = "SELECT m FROM ModuleMenuOrass m WHERE m.tailfont = :tailfont"),
//    @NamedQuery(name = "ModuleMenuOrass.findByEspafont", query = "SELECT m FROM ModuleMenuOrass m WHERE m.espafont = :espafont"),
//    @NamedQuery(name = "ModuleMenuOrass.findByStylfont", query = "SELECT m FROM ModuleMenuOrass m WHERE m.stylfont = :stylfont"),
//    @NamedQuery(name = "ModuleMenuOrass.findByEpaifont", query = "SELECT m FROM ModuleMenuOrass m WHERE m.epaifont = :epaifont"),
//    @NamedQuery(name = "ModuleMenuOrass.findByCodelang", query = "SELECT m FROM ModuleMenuOrass m WHERE m.moduleMenuOrassPK.codelang = :codelang"),
//    @NamedQuery(name = "ModuleMenuOrass.findByFlagvesi", query = "SELECT m FROM ModuleMenuOrass m WHERE m.flagvesi = :flagvesi"),
//    @NamedQuery(name = "ModuleMenuOrass.findByFlagenab", query = "SELECT m FROM ModuleMenuOrass m WHERE m.flagenab = :flagenab")})
public class ModuleMenuOrass implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ModuleMenuOrassPK moduleMenuOrassPK;
    @Basic(optional = false)
    @Column(name = "DESIMODU")
    private String desimodu;
    @Basic(optional = false)
    @Column(name = "NOM_MODU")
    private String nomModu;
    @Column(name = "MENUASSO")
    private String menuasso;
    @Column(name = "NOM_ROLE")
    private String nomRole;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Column(name = "FLAGVALI")
    private String flagvali;
    @Column(name = "FORMAPPL")
    private String formappl;
    @Column(name = "ORDRAFFI")
    private Short ordraffi;
    @Column(name = "NOM_FONT")
    private String nomFont;
    @Column(name = "TAILFONT")
    private Short tailfont;
    @Column(name = "ESPAFONT")
    private Short espafont;
    @Column(name = "STYLFONT")
    private Short stylfont;
    @Column(name = "EPAIFONT")
    private Short epaifont;
    @Column(name = "FLAGVESI")
    private String flagvesi;
    @Column(name = "FLAGENAB")
    private String flagenab;

    public ModuleMenuOrass() {
    }

    public ModuleMenuOrass(ModuleMenuOrassPK moduleMenuOrassPK) {
        this.moduleMenuOrassPK = moduleMenuOrassPK;
    }

    public ModuleMenuOrass(ModuleMenuOrassPK moduleMenuOrassPK, String desimodu, String nomModu) {
        this.moduleMenuOrassPK = moduleMenuOrassPK;
        this.desimodu = desimodu;
        this.nomModu = nomModu;
    }

    public ModuleMenuOrass(String codemenu, String codemodu, String codelang) {
        this.moduleMenuOrassPK = new ModuleMenuOrassPK(codemenu, codemodu, codelang);
    }

    public ModuleMenuOrassPK getModuleMenuOrassPK() {
        return moduleMenuOrassPK;
    }

    public void setModuleMenuOrassPK(ModuleMenuOrassPK moduleMenuOrassPK) {
        this.moduleMenuOrassPK = moduleMenuOrassPK;
    }

    public String getDesimodu() {
        return desimodu;
    }

    public void setDesimodu(String desimodu) {
        this.desimodu = desimodu;
    }

    public String getNomModu() {
        return nomModu;
    }

    public void setNomModu(String nomModu) {
        this.nomModu = nomModu;
    }

    public String getMenuasso() {
        return menuasso;
    }

    public void setMenuasso(String menuasso) {
        this.menuasso = menuasso;
    }

    public String getNomRole() {
        return nomRole;
    }

    public void setNomRole(String nomRole) {
        this.nomRole = nomRole;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public String getFlagvali() {
        return flagvali;
    }

    public void setFlagvali(String flagvali) {
        this.flagvali = flagvali;
    }

    public String getFormappl() {
        return formappl;
    }

    public void setFormappl(String formappl) {
        this.formappl = formappl;
    }

    public Short getOrdraffi() {
        return ordraffi;
    }

    public void setOrdraffi(Short ordraffi) {
        this.ordraffi = ordraffi;
    }

    public String getNomFont() {
        return nomFont;
    }

    public void setNomFont(String nomFont) {
        this.nomFont = nomFont;
    }

    public Short getTailfont() {
        return tailfont;
    }

    public void setTailfont(Short tailfont) {
        this.tailfont = tailfont;
    }

    public Short getEspafont() {
        return espafont;
    }

    public void setEspafont(Short espafont) {
        this.espafont = espafont;
    }

    public Short getStylfont() {
        return stylfont;
    }

    public void setStylfont(Short stylfont) {
        this.stylfont = stylfont;
    }

    public Short getEpaifont() {
        return epaifont;
    }

    public void setEpaifont(Short epaifont) {
        this.epaifont = epaifont;
    }

    public String getFlagvesi() {
        return flagvesi;
    }

    public void setFlagvesi(String flagvesi) {
        this.flagvesi = flagvesi;
    }

    public String getFlagenab() {
        return flagenab;
    }

    public void setFlagenab(String flagenab) {
        this.flagenab = flagenab;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (moduleMenuOrassPK != null ? moduleMenuOrassPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModuleMenuOrass)) {
            return false;
        }
        ModuleMenuOrass other = (ModuleMenuOrass) object;
        if ((this.moduleMenuOrassPK == null && other.moduleMenuOrassPK != null) || (this.moduleMenuOrassPK != null && !this.moduleMenuOrassPK.equals(other.moduleMenuOrassPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.ModuleMenuOrass[ moduleMenuOrassPK=" + moduleMenuOrassPK + " ]";
    }
    
}
