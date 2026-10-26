# Pruebas actualizadas de Programación III

## Criterios aplicados

El enunciado conserva referencias a FIFO, pero también solicita una cola con
prioridad y que el listado mantenga la prioridad. Para esta actualización, el
docente confirmó que **un menor número representa una mayor prioridad**.
Ejemplo: si llegan incidentes con prioridades 5, 1 y 3, se consultan, atienden
y listan en el orden 1, 3 y 5.

- Las prioridades del constructor son enteros, en correspondencia con el modelo actual.
- Los empates no exigen FIFO ni otro criterio de desempate; sí deben conservarse
  todos los incidentes y atenderse antes que los de menor prioridad.
- Se utiliza la capacidad recibida por el constructor. Se comprueba expresamente
  el caso de 10 elementos: aceptar el décimo y rechazar el undécimo.
- Los códigos nulos, vacíos y compuestos solo por espacios se rechazan, como en
  las pruebas anteriores. No se agregan reglas de mayúsculas o normalización.
- `consultarSiguienteIncidente()` y `listarIncidentes()` deben preservar los
  pendientes, sus estados y la disponibilidad de espacios.
- `atenderSiguienteIncidente()` debe retirar el incidente y devolverlo con estado
  `ATENDIDO`.
- Una lista devuelta puede ser una copia modificable o una lista inmodificable;
  ninguna debe permitir vaciar la cola original a través de `clear()`.
- No se impone `PENDIENTE` como estado inicial porque el enunciado lo recomienda,
  pero no lo establece como obligatorio. Tampoco se inventan rangos de prioridad,
  reglas para capacidades inválidas o validaciones adicionales de tipo y descripción.

## Casos de prueba

| Clase conservada para GitHub Classroom | Casos | Comprobaciones |
| --- | ---: | --- |
| `GestorIncidentesRegistrarTest` | 7 | Registro válido; incidente nulo sin alterar pendientes; código nulo/vacío/espacios; duplicado con datos diferentes; límite de 10; capacidad configurada de 2; reutilización de espacio tras atender. |
| `GestorIncidentesExisteTest` | 5 | Encontrar códigos con distintas prioridades sin alterar la cola; cola vacía; código inexistente; consultas nulas/vacías/espacios; exclusión del atendido. |
| `GestorIncidentesConsultarTest` | 4 | Elegir prioridad 1 aunque llegue después; consultas repetidas sin eliminar ni cambiar estado; cola vacía; actualización del siguiente tras registro y atención. |
| `GestorIncidentesAtenderTest` | 4 | Atención completa en orden 1, 3, 5 con estado y contadores; empates sin imponer desempate; preservar el estado de otros pendientes; cola vacía sin alterar contadores. |
| `GestorIncidentesContarTest` | 4 | Cero inicial; incremento hasta 10; estabilidad ante consultas/listados/rechazos; decremento hasta cero y atención adicional en vacío. |
| `GestorIncidentesEspaciosTest` | 4 | Capacidad inicial configurable; disminución hasta cero y rechazo sin valores negativos; recuperación y reutilización; ejemplo de 2 pendientes en capacidad 10. |
| `GestorIncidentesListarTest` | 6 | Lista vacía no nula; listado completo ordenado y repetible sin alterar la cola; empates; independencia estructural de la lista; conservación de estados; exclusión de atendidos. |
| **Total** | **34** | |

El caso de listado con prioridades de entrada `5, 1, 4, 2, 3` detecta que iterar
directamente una `PriorityQueue` o convertirla directamente en lista no garantiza
el orden de prioridad de todos sus elementos.

## Ejecución

Con JDK 17 y Maven, desde la raíz del proyecto:

```bash
mvn test
```

Para ejecutar solo una categoría, se mantienen los nombres que utiliza el flujo
original de GitHub Classroom, por ejemplo:

```bash
mvn -Dtest=GestorIncidentesListarTest test
```

## Estado del proyecto resuelto y verificación

En el proyecto resuelto ya están implementados los getters, setters y el constructor
con estado inicial `PENDIENTE` de `IncidenteSeguridad`, los siete métodos de
`GestorIncidentes` y la interfaz manual de Swing `InterfazIncidentes`.

Se compilaron todas las clases principales con `javac --release 17`. Se ejecutaron
las 34 pruebas incluidas mediante un ejecutor local que reproduce las aserciones
utilizadas por las pruebas JUnit 5, y el resultado fue **34 aprobadas, 0 fallidas**.
Este entorno no contaba con Maven ni con las bibliotecas originales de JUnit 5;
por ello, esta verificación no debe confundirse con una ejecución literal de
`mvn test`. El repositorio está configurado para que las pruebas reales de JUnit
se ejecuten con Maven en GitHub Classroom o en un equipo con Maven instalado.

Se verificó adicionalmente el inicio de la interfaz bajo un servidor gráfico virtual,
sin errores reportados en el arranque.

## Alcance de los cambios

Se conservaron las siete clases de pruebas recibidas. Se implementaron los archivos
principales de código fuente, se agregó `InterfazIncidentes.java`, se eliminaron
los archivos vacíos obsoletos `Ventana.java` y `Ventana.form`, se ajustó Maven a
Java 17 y se añadió `INSTRUCCIONES_EJECUCION.md`.
