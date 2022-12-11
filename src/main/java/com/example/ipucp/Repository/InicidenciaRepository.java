package com.example.ipucp.Repository;

import com.example.ipucp.Dto.IncidenciaEstado;
import com.example.ipucp.Dto.IncidenciaPorMes;
import com.example.ipucp.Dto.IncidenciaUrgencia;
import com.example.ipucp.Entity.Comentario;
import com.example.ipucp.Entity.Inicidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.List;

@Repository
public interface InicidenciaRepository extends JpaRepository<Inicidencia, Integer> {

    @Query(value="select * from inicidencia where codigo= ?1 order by fecha desc;",nativeQuery = true)
    List<Inicidencia> userIncidencias(String codigo);

    @Query(value="select * from inicidencia where (idinicidencia= ?1 and codigo =?2) ;",nativeQuery = true)
    Inicidencia obtenerIncidenciaUsuario(int id, String codigo);

    @Query(value="select * from inicidencia where (idinicidencia= ?1 and (estado='0' or estado='1' or estado='2')) ;",nativeQuery = true)
    Inicidencia obtenerIncidenciaSinResolver(int id);

    @Query(value="SELECT estado, count(idinicidencia) as cantidad FROM inicidencia group by estado order by estado desc", nativeQuery = true)
    List<IncidenciaEstado> bucarEstadoIncidencia();

    @Query(value = "SELECT u.tipo_urgencia , count(i.idinicidencia) as cantidad FROM inicidencia i inner join urgencia u where i.idurgencia = u.idurgencia group by i.idurgencia order by i.idurgencia asc",nativeQuery = true)
    List<IncidenciaUrgencia> buscarUrgenciaIncidencia();

    @Transactional
    @Modifying
    @Query(value = "UPDATE inicidencia SET `comentario` = ?1 , `estado` = '2', `max` = ?2 WHERE (`idinicidencia` = ?3);",nativeQuery = true)
    void comentarIncidencia(String comentario, int max ,int id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE inicidencia SET `comentario_usuario` = ?1 , `estado` = '1', `max_usuario` = ?2 WHERE (`idinicidencia` = ?3);",nativeQuery = true)
    void comentarIncidenciaUsuario(String comentario, int max ,int id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE `ipucp`.`inicidencia` SET `estado` = '3' WHERE (`idinicidencia` = ?1);",nativeQuery = true)
    void cambiarEstadoIncidencia(int id);

    @Query(value = "SELECT  t.tipo_incidencia as numero FROM inicidencia i right join tipo t on i.idtipo = t.idtipo group by t.idtipo;",nativeQuery = true)
    List<String> buscarTipoIncidencia();

    @Query(value = "SELECT count(i.idinicidencia) as numero FROM inicidencia i right join tipo t on i.idtipo = t.idtipo group by t.idtipo;",nativeQuery = true)
    List<Integer> buscarCantidadIncidencia();

    @Query(value="SELECT * FROM inicidencia where idtipo = ?1  order by idinicidencia desc;",nativeQuery = true)
    List<Inicidencia> filtradoTipo(int idTipo);

    @Query(value = "SELECT * FROM inicidencia where idtipo = ?1 and idurgencia = ?2  order by idinicidencia desc;",nativeQuery = true)
    List<Inicidencia> filtradoTipoUrgencia(int idTipo, int idUrgencia);

    @Query(value = "SELECT * FROM inicidencia where idurgencia = ?1  order by idinicidencia desc;",nativeQuery = true)
    List<Inicidencia> filtradoUrgencia(int idUrgencia);

    @Query(value = "SELECT * FROM inicidencia where (estado='0' or estado='1' or estado='2') ORDER BY idinicidencia desc;", nativeQuery = true)
    List<Inicidencia> orderReciente();

    @Query(value = "SELECT * FROM inicidencia where (estado='0' or estado='1' or estado='2') ORDER BY destacado desc;", nativeQuery = true)
    List<Inicidencia> orderMaspopular();

    @Transactional
    @Modifying
    @Query(value = "update inicidencia set destacado = destacado + 1 where (`idinicidencia` = ?);",nativeQuery = true)
    void destacarIncidencia(int id);

    @Query(value="SELECT * FROM inicidencia order by idinicidencia desc",nativeQuery = true)
    List<Inicidencia> ordenNuevo();

    @Query(value = "SELECT * FROM inicidencia where idurgencia = ?1   order by idinicidencia;",nativeQuery = true)
    List<Inicidencia> filtradoUrgenciaAntiguo(int idUrgencia);

    @Query(value="SELECT * FROM inicidencia where idtipo = ?1  order by idinicidencia;",nativeQuery = true)
    List<Inicidencia> filtradoTipoAntiguo(int idTipo);

    @Query(value = "SELECT * FROM inicidencia where idtipo = ?1 and idurgencia = ?2 order by idinicidencia;",nativeQuery = true)
    List<Inicidencia> filtradoTipoUrgenciaAntig(int idTipo, int idUrgencia);

    @Query(value = "SELECT DATE_SUB(now(), INTERVAL 5 HOUR);",nativeQuery = true)
    Instant fecha();

    @Query(value = "SELECT month(fecha) as 'mes',count(idinicidencia) as 'cantidad' FROM ipucp.inicidencia group by month(fecha) ",nativeQuery = true)
    List<IncidenciaPorMes> incidenciaMes();

    /*filtros 2*/
    @Query(value = "SELECT * FROM inicidencia where idtipo = ?1 and idurgencia = ?2 and estado = ?3 order by idinicidencia;",nativeQuery = true)
    List<Inicidencia> filtradoTipoUrgenciaAntigEstado(int idTipo, int idUrgencia, int estado);

    @Query(value = "SELECT * FROM inicidencia where idtipo = ?1 and idurgencia = ?2 and estado = ?3 order by idinicidencia desc;",nativeQuery = true)
    List<Inicidencia> filtradoTipoUrgenciaEstado(int idTipo, int idUrgencia, int estado);

    @Query(value="SELECT * FROM inicidencia where idtipo = ?1 and estado = ?2 order by idinicidencia;",nativeQuery = true)
    List<Inicidencia> filtradoTipoAntiguoEstado(int idTipo, int estado);

    @Query(value="SELECT * FROM inicidencia where idtipo = ?1 and estado = ?2  order by idinicidencia desc;",nativeQuery = true)
    List<Inicidencia> filtradoTipoEstado(int idTipo, int estado);

    @Query(value = "SELECT * FROM inicidencia where idurgencia = ?1 and estado = ?2  order by idinicidencia;",nativeQuery = true)
    List<Inicidencia> filtradoUrgenciaAntiguoEstado(int idUrgencia,int estado);

    @Query(value = "SELECT * FROM inicidencia where idurgencia = ?1 and estado = ?2 order by idinicidencia desc;",nativeQuery = true)
    List<Inicidencia> filtradoUrgenciaEstado(int idUrgencia, int estado);

    @Query(value="SELECT * FROM inicidencia where estado = ?1 order by idinicidencia desc",nativeQuery = true)
    List<Inicidencia> ordenNuevoEstaodo(int estado);

    @Query(value="SELECT * FROM inicidencia where estado = ?1 order by idinicidencia asc",nativeQuery = true)
    List<Inicidencia> ordenAntigEstaodo(int estado);
}