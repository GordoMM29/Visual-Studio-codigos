const express = require('express');
const cors = require('cors');
const { Pool } = require('pg');
const multer = require('multer');
const fs = require('fs');
const https = require('https');
const path = require('path');

const app = express();
app.use(cors());
app.use(express.json());
app.use(express.static('public')); // sirve archivos de /public

// Configuración de multer para subir fotos
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, 'public/fotos/');
    },
    filename: (req, file, cb) => {
        const nombreArchivo = Date.now() + path.extname(file.originalname);
        cb(null, nombreArchivo);
    }
});
const upload = multer({ storage });

// Conexión PostgreSQL
const pool = new Pool({
    user: 'postgres',
    host: 'localhost',
    database: 'RRHH',
    password: '2211057088',
    port: 5432
});

// Ruta test
app.get('/', (req, res) => {
    res.send('Servidor Node.js funcionando 🚀');
});

// Función para descargar imagen desde URL
function descargarImagen(url, destino) {
    return new Promise((resolve, reject) => {
        const file = fs.createWriteStream(destino);
        https.get(url, (response) => {
            response.pipe(file);
            file.on('finish', () => file.close(resolve));
        }).on('error', (err) => {
            fs.unlink(destino, () => reject(err));
        });
    });
}

// Crear evento
app.post('/api/eventos', upload.single('imagen_file'), async (req, res) => {
    const e = req.body;  // campos de texto
    const file = req.file; // archivo subido
    let imagenRuta = null;

    console.log("🖼 Archivo recibido:", req.file);
    console.log("🌐 URL recibida:", req.body.imagen_url);

    try {
        // Si se subió un archivo
        if (file) {
            imagenRuta = `/fotos/${file.filename}`;
        }
        // Si se pegó una URL
        else if (e.imagen_url && e.imagen_url.startsWith('http')) {
            const nombreArchivo = Date.now() + '.jpg';
            const destino = path.join('public/fotos/', nombreArchivo);
            await descargarImagen(e.imagen_url, destino);
            imagenRuta = `/fotos/${nombreArchivo}`;
        }

        console.log("📸 Ruta final guardada:", imagenRuta);

        // Guardamos en PostgreSQL
        const result = await pool.query(
            `INSERT INTO evento
            (id_organizador, nombre_evento, fecha_hora, lugar, descripcion_corta, descripcion_larga, cupos, costo, invitado, id_estado, id_tipo, imagen_url)
            VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10,$11,$12) RETURNING *`,
            [
                parseInt(e.id_organizador),
                e.nombre_evento,
                e.fecha_hora,
                e.lugar,
                e.descripcion_corta,
                e.descripcion_larga,
                parseInt(e.cupos) || 0,
                parseFloat(e.costo) || 0,
                e.invitado,
                parseInt(e.id_estado) || null,
                parseInt(e.id_tipo) || null,
                imagenRuta
            ]
        );

        res.json({ success: true, evento: result.rows[0] });
    } catch (err) {
        console.error(err);
        res.status(500).json({ success: false, message: err.message });
    }
});

// Obtener eventos por tipo
app.get('/api/eventos/tipo/:idTipo', async (req, res) => {
    const idTipo = parseInt(req.params.idTipo);
    try {
        const result = await pool.query(
            `SELECT * FROM evento WHERE id_tipo = $1`, [idTipo]
        );
        res.json({ success: true, eventos: result.rows });
    } catch (err) {
        res.status(500).json({ success: false, message: err.message });
    }
});

app.listen(3000, () => {
    console.log('Servidor corriendo en http://localhost:3000');
});
