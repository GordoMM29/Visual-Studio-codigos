<?php
$servidor = "localhost";
$usuario = "root";
$clave = "";
$baseDatos = "escuela"; 

$conexion = mysqli_connect($servidor, $usuario, $clave, $baseDatos);

if (!$conexion) {
    die("Conexión fallida: " . mysqli_connect_error());
}
echo "Conexión exitosa";