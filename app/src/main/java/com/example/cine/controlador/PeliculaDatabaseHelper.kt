package com.example.cine.controlador

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.cine.modelo.Horario
import com.example.cine.modelo.Pelicula
import com.example.cine.modelo.Reserva
import com.example.cine.modelo.ReservaDetallada
import com.example.cine.modelo.User

class PeliculaDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "peliculas.db"
        private const val DATABASE_VERSION = 11 // Incrementamos la versión
        const val TABLE_NAME = "peliculas"
        const val TABLE_HORARIOS = "horarios"
        const val TABLE_USUARIOS = "usuarios"
        const val TABLE_RESERVAS = "reservas"  // Nueva tabla para reservas

        const val COLUMN_ID = "id"
        const val COLUMN_TITULO = "titulo"
        const val COLUMN_DESCRIPCION = "descripcion"
        const val COLUMN_IMAGEN = "imagen"
        const val COLUMN_TRES_D = "tresD"
        const val COLUMN_RESTRICCION_EDAD = "restriccionEdad"
        const val COLUMN_HORA_INICIO = "horaInicio"
        const val COLUMN_HORA_FIN = "horaFin"
        const val COLUMN_PELICULA_ID = "pelicula_id"
        const val COLUMN_USUARIO_ID = "usuario_id"
        const val COLUMN_HORARIO_ID = "horario_id"
        const val COLUMN_CORREO = "correo"
        const val COLUMN_CONTRASENA = "contrasena"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createPeliculasTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITULO TEXT NOT NULL,
                $COLUMN_DESCRIPCION TEXT NOT NULL,
                $COLUMN_IMAGEN TEXT NOT NULL,
                $COLUMN_TRES_D INTEGER NOT NULL,
                $COLUMN_RESTRICCION_EDAD TEXT NOT NULL
            )
        """
        db.execSQL(createPeliculasTableQuery)

        val createHorariosTableQuery = """
            CREATE TABLE $TABLE_HORARIOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_HORA_INICIO TEXT NOT NULL,
                $COLUMN_HORA_FIN TEXT NOT NULL,
                $COLUMN_PELICULA_ID INTEGER NOT NULL,
                FOREIGN KEY($COLUMN_PELICULA_ID) REFERENCES $TABLE_NAME($COLUMN_ID)
            )
        """
        db.execSQL(createHorariosTableQuery)

        val createUsuariosTableQuery = """
            CREATE TABLE $TABLE_USUARIOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CORREO TEXT NOT NULL UNIQUE,
                $COLUMN_CONTRASENA TEXT NOT NULL
            )
        """
        db.execSQL(createUsuariosTableQuery)

        val createReservasTableQuery = """
            CREATE TABLE $TABLE_RESERVAS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USUARIO_ID INTEGER NOT NULL,
                $COLUMN_PELICULA_ID INTEGER NOT NULL,
                $COLUMN_HORARIO_ID INTEGER NOT NULL,
                FOREIGN KEY($COLUMN_USUARIO_ID) REFERENCES $TABLE_USUARIOS($COLUMN_ID),
                FOREIGN KEY($COLUMN_PELICULA_ID) REFERENCES $TABLE_NAME($COLUMN_ID),
                FOREIGN KEY($COLUMN_HORARIO_ID) REFERENCES $TABLE_HORARIOS($COLUMN_ID)
            )
        """
        db.execSQL(createReservasTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RESERVAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_HORARIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Método para insertar un nuevo usuario
    fun insertUser(user: User): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CORREO, user.correo)
            put(COLUMN_CONTRASENA, user.constrasena)
        }
        return db.insert(TABLE_USUARIOS, null, values)
    }

    // Método para verificar si el usuario y la contraseña son correctos
    fun validateUser(correo: String, contrasena: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            null,
            "$COLUMN_CORREO = ? AND $COLUMN_CONTRASENA = ?",
            arrayOf(correo, contrasena),
            null,
            null,
            null
        )
        val isValid = cursor.moveToFirst()  // Si el cursor tiene un resultado, el usuario es válido
        cursor.close()
        return isValid
    }

    // Método para insertar una película
    fun insertPelicula(pelicula: Pelicula): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITULO, pelicula.tituloPelicula)
            put(COLUMN_DESCRIPCION, pelicula.descripcionPelicula)
            put(COLUMN_IMAGEN, pelicula.imagenPelicula)
            put(COLUMN_TRES_D, if (pelicula.tresD) 1 else 0)
            put(COLUMN_RESTRICCION_EDAD, pelicula.restriccionDeEdad)
        }
        val peliculaId = db.insert(TABLE_NAME, null, values)

        // Insertar horarios de la película
        pelicula.horario.forEach { horario ->
            val horarioValues = ContentValues().apply {
                put(COLUMN_HORA_INICIO, horario.horaInicio)
                put(COLUMN_HORA_FIN, horario.horaFin)
                put(COLUMN_PELICULA_ID, peliculaId)
            }
            db.insert(TABLE_HORARIOS, null, horarioValues)
        }

        return peliculaId
    }

    // Método para obtener todas las películas con sus horarios
    fun getPeliculas(): ArrayList<Pelicula> {
        val db = readableDatabase
        val peliculas = ArrayList<Pelicula>()
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val titulo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITULO))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPCION))
            val imagen = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGEN))
            val tresD = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TRES_D)) == 1
            val restriccionEdad =
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESTRICCION_EDAD))

            val pelicula = Pelicula(
                titulo,
                descripcion,
                imagen,
                ArrayList(),
                tresD,
                restriccionEdad
            )

            // Obtener los horarios de esta película
            val horariosCursor = db.query(
                TABLE_HORARIOS,
                null,
                "$COLUMN_PELICULA_ID = ?",
                arrayOf(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))),
                null,
                null,
                null
            )

            while (horariosCursor.moveToNext()) {
                val horaInicio =
                    horariosCursor.getString(horariosCursor.getColumnIndexOrThrow(COLUMN_HORA_INICIO))
                val horaFin =
                    horariosCursor.getString(horariosCursor.getColumnIndexOrThrow(COLUMN_HORA_FIN))
                pelicula.horario.add(Horario(horaInicio, horaFin))
            }

            horariosCursor.close()
            peliculas.add(pelicula)
        }
        cursor.close()
        return peliculas
    }

    fun insertReservaPorDetalles(
        correo: String,
        tituloPelicula: String,
        horaInicio: String,
        horaFin: String
    ): Long {
        val db = writableDatabase

        // Obtener el ID del usuario a partir de su correo
        val usuarioIdQuery = "SELECT $COLUMN_ID FROM $TABLE_USUARIOS WHERE $COLUMN_CORREO = ?"
        val usuarioCursor = db.rawQuery(usuarioIdQuery, arrayOf(correo))
        if (!usuarioCursor.moveToFirst()) {
            usuarioCursor.close()
            throw IllegalArgumentException("Usuario no encontrado con el correo: $correo")
        }
        val usuarioId = usuarioCursor.getInt(usuarioCursor.getColumnIndexOrThrow(COLUMN_ID))
        usuarioCursor.close()

        // Obtener el ID de la película a partir de su título
        val peliculaIdQuery = "SELECT $COLUMN_ID FROM $TABLE_NAME WHERE $COLUMN_TITULO = ?"
        val peliculaCursor = db.rawQuery(peliculaIdQuery, arrayOf(tituloPelicula))
        if (!peliculaCursor.moveToFirst()) {
            peliculaCursor.close()
            throw IllegalArgumentException("Película no encontrada con el título: $tituloPelicula")
        }
        val peliculaId = peliculaCursor.getInt(peliculaCursor.getColumnIndexOrThrow(COLUMN_ID))
        peliculaCursor.close()

        // Obtener el ID del horario a partir de la hora de inicio y la hora de fin
        val horarioIdQuery =
            "SELECT $COLUMN_ID FROM $TABLE_HORARIOS WHERE $COLUMN_HORA_INICIO = ? AND $COLUMN_HORA_FIN = ?"
        val horarioCursor = db.rawQuery(horarioIdQuery, arrayOf(horaInicio, horaFin))
        if (!horarioCursor.moveToFirst()) {
            horarioCursor.close()
            throw IllegalArgumentException("Horario no encontrado para la hora de inicio: $horaInicio y hora de fin: $horaFin")
        }
        val horarioId = horarioCursor.getInt(horarioCursor.getColumnIndexOrThrow(COLUMN_ID))
        horarioCursor.close()

        // Insertar la reserva
        val values = ContentValues().apply {
            put(COLUMN_USUARIO_ID, usuarioId)
            put(COLUMN_PELICULA_ID, peliculaId)
            put(COLUMN_HORARIO_ID, horarioId)
        }
        return db.insert(TABLE_RESERVAS, null, values)
    }


    // Método para obtener reservas por usuario
    fun getReservasByUsuario(usuarioId: Int): ArrayList<Reserva> {
        val db = readableDatabase
        val reservas = ArrayList<Reserva>()
        val cursor = db.query(
            TABLE_RESERVAS,
            null,
            "$COLUMN_USUARIO_ID = ?",
            arrayOf(usuarioId.toString()),
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val peliculaId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PELICULA_ID))
            val horarioId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HORARIO_ID))
            reservas.add(Reserva(id, usuarioId, peliculaId, horarioId))
        }
        cursor.close()
        return reservas
    }


    // Método para obtener los detalles de las reservas
    fun getReservasDetalladas(usuarioId: Int): ArrayList<Map<String, String>> {
        val db = readableDatabase
        val reservasDetalladas = ArrayList<Map<String, String>>()

        val query = """
        SELECT 
            p.$COLUMN_TITULO AS titulo,
            h.$COLUMN_HORA_INICIO AS horaInicio,
            h.$COLUMN_HORA_FIN AS horaFin
        FROM $TABLE_RESERVAS r
        INNER JOIN $TABLE_NAME p ON r.$COLUMN_PELICULA_ID = p.$COLUMN_ID
        INNER JOIN $TABLE_HORARIOS h ON r.$COLUMN_HORARIO_ID = h.$COLUMN_ID
        WHERE r.$COLUMN_USUARIO_ID = ?
    """

        val cursor = db.rawQuery(query, arrayOf(usuarioId.toString()))

        while (cursor.moveToNext()) {
            val titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"))
            val horaInicio = cursor.getString(cursor.getColumnIndexOrThrow("horaInicio"))
            val horaFin = cursor.getString(cursor.getColumnIndexOrThrow("horaFin"))
            reservasDetalladas.add(
                mapOf(
                    "titulo" to titulo,
                    "horaInicio" to horaInicio,
                    "horaFin" to horaFin
                )
            )
        }
        cursor.close()
        return reservasDetalladas
    }

    fun getReservasDetalladasPorCorreo(correo: String): ArrayList<ReservaDetallada> {
        val db = readableDatabase
        val reservasDetalladas = ArrayList<ReservaDetallada>()

        val query = """
        SELECT 
            p.$COLUMN_TITULO AS titulo,
            h.$COLUMN_HORA_INICIO AS horaInicio,
            h.$COLUMN_HORA_FIN AS horaFin
        FROM $TABLE_RESERVAS r
        INNER JOIN $TABLE_NAME p ON r.$COLUMN_PELICULA_ID = p.$COLUMN_ID
        INNER JOIN $TABLE_HORARIOS h ON r.$COLUMN_HORARIO_ID = h.$COLUMN_ID
        INNER JOIN $TABLE_USUARIOS u ON r.$COLUMN_USUARIO_ID = u.$COLUMN_ID
        WHERE u.$COLUMN_CORREO = ?
    """

        val cursor = db.rawQuery(query, arrayOf(correo))

        while (cursor.moveToNext()) {
            val titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"))
            val horaInicio = cursor.getString(cursor.getColumnIndexOrThrow("horaInicio"))
            val horaFin = cursor.getString(cursor.getColumnIndexOrThrow("horaFin"))
            reservasDetalladas.add(ReservaDetallada(titulo, horaInicio, horaFin))
        }
        cursor.close()
        return reservasDetalladas
    }


}
