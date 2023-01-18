<?php
$conexion = mysqli_connect("localhost", "id20167342_fragargar15", "LsqRWJ=?QN38|6&P", "id20167342_bdkepido");

$correo = $_POST["correo"];
$contraseña = $_POST["contraseña"];

$sql = "SELECT * FROM usuarios WHERE correo='$correo' AND contraseña='$contraseña'";
$result = mysqli_query($conexion,$sql);

if($result->num_rows>0){
	echo "ingresaste correctamente";
}
else{
	echo "No pudo ingresar";
}

?>