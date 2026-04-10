<?php
header('Content-Type: application/json; charset=utf-8');
$servidor = 'localhost';
$banco      = 'correios_mobile';
$usuario  = 'root';
$senha    = '';

$conexao = mysqli_connect($servidor, $usuario, $senha, $banco);

$json = file_get_contents('php://input');
$obj = json_decode($json);

$texto1=$obj->codigo_rastreio;	
$texto2=$obj->empresa;
$texto3=$obj->servico;
$texto4=$obj->status;
$texto5=$obj->origem;
$texto6=$obj->destino;
$texto7=$obj->data_previsao;

$sql ="INSERT INTO produto(codigorastreio, empresa, servico, status, origem, destino, datadeprevistadeentraga) VALUES ('".$texto1."','".$texto2."','".$texto3."','".$texto4."','".$texto5."','".$texto6."','".$texto7."')";
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