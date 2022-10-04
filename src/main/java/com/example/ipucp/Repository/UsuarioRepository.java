package com.example.ipucp.Repository;

import com.example.ipucp.Entity.Usuario;
import com.example.ipucp.Dto.UsuarioIncidencias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    @Query(value = "SELECT * FROM usuario where idcargo=?1",
            nativeQuery = true)
    List<Usuario> buscarUsuarioPorCat(Integer id);

    @Query(value = "select inc.idinicidencia, tip.tipo_incidencia, inc.estado\n" +
            "from inicidencia inc, tipo tip\n" +
            "where inc.idtipo=tip.idtipo\n" +
            "and inc.codigo=?1",
            nativeQuery = true)
    List<UsuarioIncidencias> obtenerUsuarioIncidencias(String id);

    @Query(value = "SELECT * FROM usuario \n" +
            "where (idcargo='1' or\n" +
            "idcargo='3' or idcargo='4'\n" +
            "or idcargo='5') and estado='1'",
            nativeQuery = true)
    List<Usuario> listarUsuarios();

    @Transactional
    @Modifying
    @Query(value = "UPDATE `ipucp`.`usuario` SET `ban` = '0', `strikes` = '0', `justificacion` = '' WHERE `codigo` = (?1);",nativeQuery = true)
    void habilitarUsuario(String codigo);

    @Transactional
    @Modifying
    @Query(value = "UPDATE `ipucp`.`usuario` SET `ban` = '1', `justificacion` = (?1) WHERE `codigo` = (?2);",nativeQuery = true)
    void suspenderUsuario(String justificacion, String codigo);

    @Query(value="select * from usuario where codigo= ?1 ;",nativeQuery = true)
    Usuario userPerfil(String codigo);

    @Transactional
    @Modifying
    @Query(value = "UPDATE `ipucp`.`usuario` SET `contra` = SHA2((?1),256) WHERE (`codigo` = (?2));",nativeQuery = true)
    void cifradoHash(String contra, String codigo);

    @Transactional
    @Modifying
    @Query(value = "UPDATE `ipucp`.`usuario` SET `strikes` = ?1 WHERE (`codigo` = ?2);\n",nativeQuery = true)
    void strikeUsuario(int strike, String id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE `ipucp`.`usuario` SET `ban` = ?1 WHERE (`codigo` = ?2);\n",nativeQuery = true)
    void banUsuario(int num, String id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE `ipucp`.`usuario` SET `contra` = ?1 , `estado` = '1' WHERE (`codigo` = ?2)",nativeQuery = true)
    void registrar(String contrasenha, String codigo);


    Usuario findByCorreo(String correo);



}