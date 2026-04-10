<?php
header('Content-Type: application/json; charset=utf-8');
$servidor = 'localhost';
$banco      = 'correios_mobile';
$usuario  = 'root';
$senha    = '';
$json = file_get_contents('php://input');
$obj = json_decode($json);
$texto1=$obj->id;	
$conexao = mysqli_connect($servidor, $usuario, $senha, $banco);
$dados = mysqli_query($conexao,"SELECT data, local, status, detalhes FROM evento WHERE produto_id = '$texto1' ORDER BY id");
$vetor = array();
$vetor['evento'] = array();

while ($func = mysqli_fetch_assoc($dados)):
 $vetor['evento'][]=array_map('utf8_encode',$func);
 endwhile;
 echo json_encode($vetor);
 
?>