<?php
header('Content-Type: application/json; charset=utf-8');
$servidor = 'localhost';
$banco      = 'correios_mobile';
$usuario  = 'root';
$senha    = '';

$conexao = mysqli_connect($servidor, $usuario, $senha, $banco);

$json = file_get_contents('php://input');
$obj = json_decode($json);

$texto1=$obj->produto_id;	
$texto2=$obj->data;
$texto3=$obj->local;
$texto4=$obj->status;
$texto5=$obj->detalhes;

$sql ="INSERT INTO evento(produto_id, data, local, status, detalhes) VALUES ('".$texto1."','".$texto2."','".$texto3."','".$texto4."','".$texto5."')";
if (mysqli_query($conexao,$sql) == true){
    $retorno = array(
        'status' => 'ok'
    );
    echo json_encode($retorno);
}
else{
    $retorno = array(
        'status' => 'erro');
        echo json_encode($retorno);
}
?>