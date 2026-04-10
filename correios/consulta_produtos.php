<?php
header('Content-Type: application/json; charset=utf-8');
$servidor = 'localhost';
$banco      = 'correios_mobile';
$usuario  = 'root';
$senha    = '';

$conexao = mysqli_connect($servidor, $usuario, $senha, $banco);
$vetor = array();
$vetor['produto'] = array();
$json = file_get_contents('php://input');
$obj = json_decode($json);
$dados = mysqli_query($conexao, "SELECT id, codigorastreio FROM produto");
while ($func = mysqli_fetch_assoc($dados)):
 $vetor['produto'][]=array_map('utf8_encode',$func);
 endwhile;
 echo json_encode($vetor);
 
?>