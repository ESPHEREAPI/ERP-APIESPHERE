/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repositories;

import db.biometry.biometry.entite.Permission;
import db.biometry.biometry.entite.RolePermissions;
import db.biometry.biometry.entite.Roles;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 *
 * @author USER01
 */
@Repository
public interface RolePermissionsRepositorie extends JpaRepository<RolePermissions, Long> {

  @Query("SELECT rp FROM RolePermissions rp WHERE rp.role.id = :roleId AND rp.permission.id = :permissionId")
public RolePermissions findByRoleAndPermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);


    public List<RolePermissions> findByRole(Roles role);

    @Query("SELECT rp.permission FROM RolePermissions rp  WHERE rp.role.id= :roleid")
    public List<Permission> listePermissionsByRoles(@Param("roleid") Long roleid);
}
