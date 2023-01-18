<?php
$conexion = mysqli_connect("localhost", "id20167342_fragargar15", "LsqRWJ=?QN38|6&P", "id20167342_bdkepido");

$usuario = $_POST["usuario"];
$correo = $_POST["correo"];
$nombre = $_POST["nombre"];
$contraseña = $_POST["contraseña"];


$sql = "INSERT INTO usuarios(usuario, correo, nombre, contraseña) VALUES ('$usuario', '$correo', '$nombre', '$contraseña')";
$result = mysqli_query($conexion,$sql);

if($result){
	echo "Datos insertados";
}
else{
	echo "No pudo insertar";
}



?>