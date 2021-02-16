package br.com.opah.ficha_proposta.service;


import br.com.opah.ficha_proposta.client.*;
import br.com.opah.ficha_proposta.client.autorizador.AutorizadorClient;
import br.com.opah.ficha_proposta.client.empresa.EmpresaClient;
import br.com.opah.ficha_proposta.client.empresa.EmpresaResponse;
import br.com.opah.ficha_proposta.client.funcionario.FuncionarioClient;
import br.com.opah.ficha_proposta.client.funcionario.FuncionarioResponse;
import br.com.opah.ficha_proposta.client.integracao.IntegracaoClient;
import br.com.opah.ficha_proposta.client.motor.MatrizDefinicaoLimiteRequest;
import br.com.opah.ficha_proposta.client.motor.MatrizDefinicaoLimiteResponse;
import br.com.opah.ficha_proposta.client.motor.MotorClient;
import br.com.opah.ficha_proposta.client.sms.ClienteSms;
import br.com.opah.ficha_proposta.client.sms.SmsRequest;
import br.com.opah.ficha_proposta.dto.*;
import br.com.opah.ficha_proposta.dtoBureau.ResultadoConsulta;
import br.com.opah.ficha_proposta.dtoPrevencaoFraude.*;
import br.com.opah.ficha_proposta.entity.*;
import br.com.opah.ficha_proposta.exception.ContatoInvalidoException;
import br.com.opah.ficha_proposta.exception.FaturaException;
import br.com.opah.ficha_proposta.exception.PropostaInvalidaException;
import br.com.opah.ficha_proposta.exception.RequestBureauException;
import br.com.opah.ficha_proposta.mapping.*;
import br.com.opah.ficha_proposta.util.CreateBarcodePdf;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.io.FileUtils;
import org.bson.internal.Base64;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;


@ApplicationScoped
public class PropostaService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final static String identificacaoIdentidadeFraude = "D5D07394-E181-4C8D-AAFD-C4098E7C7B8F";

    @Inject
    PropostaMapper propostaMapper;

    @Inject
    ClienteMapper clienteMapper;

    @Inject
    CertifaceMapper certifaceMapper;

    @Inject
    PrevencaoFraudeMapper prevencaoFraudeMapper;

    @Inject
    ContatoMapper contatoMapper;

    @Inject
    EmpresaMapper empresaMapper;

    @Inject
    FuncionarioMapper funcionarioMapper;

    @Inject
    PropostaServiceUtils utils;

    @Inject
    @RestClient
    MotorClient motorClient;

    @Inject
    @RestClient
    IntegracaoClient integracaoClient;

    @Inject
    @RestClient
    AutorizadorClient autorizadorClient;

    @Inject
    @Channel("message")
    Emitter<PropostaInicialResponse> emitter;

    @Inject
    @RestClient
    ClientBureau clientBureau;

    @Inject
    @RestClient
    ClienteClient clienteClient;

    @Inject
    @RestClient
    FuncionarioClient funcionarioClient;

    @Inject
    @RestClient
    EmpresaClient empresaClient;

    /**
     * Regras de validação P1
     *
     * @param propostaMinimoRequest
     * @return
     * @throws PropostaInvalidaException
     */
    public PropostaInicialResponse criarPropostaDadosMinimo(PropostaInicialRequest propostaMinimoRequest) throws PropostaInvalidaException, RequestBureauException {

        Boolean aprovado = Boolean.FALSE;
        Boolean aprovadoCPF = Boolean.FALSE;
        Boolean aprovadoDataNascimento = Boolean.FALSE;
        P1aCpfResponse p1aCpfResponse = new P1aCpfResponse();
        P1aDataNascimentoResponse p1aDataNascimentoResponse = new P1aDataNascimentoResponse();
        P1bResponse p1bResponse = new P1bResponse();
        ArrayList<String> listaMotivos = new ArrayList<String>();

        /* valida se existem outras propostas para o CPF informado */
        utils.validarOutrasPropostasMenos90Dias(propostaMinimoRequest);

        //verifica se ja tem cartao para este cpf
        ClienteResponse clienteResponse = clienteClient.buscarClientePorCpf(propostaMinimoRequest.getCpf());
        if (clienteResponse != null) {
            throw new PropostaInvalidaException("Cliente já possui um cartão ativo.");
        }

        //persiste proposta dados minimos
        Proposta proposta = propostaMapper.fromResource(propostaMinimoRequest);
        proposta.setDataInclusao(LocalDateTime.now());
        proposta.setStatus(Status.RASCUNHO);
        proposta.setSituacao(Situacao.P1);

        //insere na proposta funcionario que esta criando a proposta
        FuncionarioResponse funcionarioResponse = funcionarioClient.buscarFuncionarioPorIdUsuario(propostaMinimoRequest.getIdUsuario());
        Funcionario atendente = new Funcionario();

        //aqui grava o ID do usuario
        atendente.setId(propostaMinimoRequest.getIdUsuario());
        atendente.setNome(funcionarioResponse.getNome());
        proposta.setAtendente(atendente);

        //insere na proposta empresa da proposta através do usuario
        EmpresaResponse empresaResponse = empresaClient.buscarEmpresa(funcionarioResponse.getIdEmpresa());
        Empresa loja = new Empresa();
        loja.setId(empresaResponse.getId().toString());
        loja.setNome(empresaResponse.getNome());
        proposta.setLoja(loja);
        proposta.persist();

        //chamar motor de regras
        Status status = Status.REPROVADA;
        ResultadoConsulta resultadoConsulta = null;
        ResultadoConsulta consultaEmpregoFormal = null;

        //fazer chamada spc com serviço
        resultadoConsulta = clientBureau.consultarSpc(utils.retornaDadosP1(proposta.getCpf()));
        /* Reprovacao automatica, item 3.1.5.C */
        proposta.setQdeConsultaUltimosDoisDias(resultadoConsulta.getQdeConsultaUltimosDoisDias());

        //buscar nome identificado no spc
        String nome = resultadoConsulta.getConsumidor().getConsumidorPessoaFisica().getNome();
        if (nome.equalsIgnoreCase("CADASTRO NAO LOCALIZADO")) {
            throw new PropostaInvalidaException("Informação do spc: " + nome);
        }

        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setCelular(propostaMinimoRequest.getCelular());
        proposta.setCliente(cliente);

        P1aCpfRequest p1aCpfRequest = new P1aCpfRequest();
        P1aDataNascimentoRequest p1aDataNascimentoRequest = new P1aDataNascimentoRequest();
        P1bRequest p1b = new P1bRequest();

        p1aCpfRequest.setCpfAnalisadoMenosNoventaDias(resultadoConsulta.getConsultaRealizada().getQuantidadeDiasConsultados() < 90);

        utils.preencheSituacaoCPF(resultadoConsulta, p1aCpfRequest);
        p1aCpfResponse = motorClient.validarPropostaP1aCPF(p1aCpfRequest);
        aprovadoCPF = p1aCpfResponse.getAprovado();

        //valida se foi aprovado pelo CPF
        if (aprovadoCPF) {
            //valida se tem data de nascimento
            if (utils.extrairDataNascimento(resultadoConsulta, proposta.getCpf()) != null) {
                //caso tenha data de nascimento valida se aprovado a data de nascimento
                p1aDataNascimentoRequest.setDataNascimento(utils.extrairDataNascimento(resultadoConsulta, proposta.getCpf()));
                p1aDataNascimentoResponse = motorClient.validarPropostaP1aDataNascimento(p1aDataNascimentoRequest);
                aprovadoDataNascimento = p1aDataNascimentoResponse.getAprovado();
                if (aprovadoDataNascimento) {
                    //caso sim foi aprovado na p1a
                    aprovado = true;
                } else {
                    //se não foi reprovado a p1a
                    p1aDataNascimentoResponse.getMotivosReprovacao().forEach(motivo -> listaMotivos.add(motivo));
                    aprovado = false;
                }
            } else {
                //caso não tenha data de nascimento foi aprovado a p1a
                aprovado = true;
                listaMotivos.add("Data nascimento não encontrada!");
            }
        } else {
            p1aCpfResponse.getMotivosReprovacao().forEach(motivo -> listaMotivos.add(motivo));
        }

        //caso aprovado no p1a segue
        if (aprovado) {
            try {
                consultaEmpregoFormal = clientBureau.consultarSpc(utils.retornaDadosP2(proposta.getCpf()));
            } catch (Exception e) {
                throw new PropostaInvalidaException(e.getMessage());
            }
            p1b.setEmpregoFormal(consultaEmpregoFormal.isEmpregoFormal());
            resultadoConsulta.setRestricao(consultaEmpregoFormal.isRestricao());
            resultadoConsulta.setProfissao(consultaEmpregoFormal.getProfissao());
            p1b.setScore(resultadoConsulta.getScore());
            p1bResponse = motorClient.validarPropostaP1B(p1b);
            aprovado = p1bResponse.getAprovado();
            //caso aprovado na p1b segue
            if (aprovado) {
                status = Status.APROVADA;
                proposta.setDataAprovacao(LocalDateTime.now());
            } else {
                proposta.setDataReprovacao(LocalDateTime.now());
                atualizarStatusProposta(proposta, status);

                //se reprovada verifica os motivos da reprovacao e gera exception
                P1bResponse finalP1bResponse = p1bResponse;
                p1bResponse.getMotivosReprovacao().forEach(motivo -> listaMotivos.add(motivo + " - " + finalP1bResponse.getScore()));
            }
        }

        //se reprovoda atualiza a data da reprovacao
        if (status.equals(Status.REPROVADA)) proposta.setDataReprovacao(LocalDateTime.now());

        //atualiza informacoes da proposta
        atualizarStatusProposta(proposta, status);

        //se reprovada verifica os motivos da reprovacao e salva
        Status finalStatus = status;
        listaMotivos.forEach(motivo -> utils.salvarMotivosStatus(
                motivo,
                finalStatus,
                proposta.getId().toString(),
                proposta.getCpf()
                )
        );

        //cria response com dados da proposta e spc
        PropostaInicialResponse response = utils.preencherPropostaInicial(proposta.getId().toString(), status.toString(), resultadoConsulta);
        response.setNegativacao(consultaEmpregoFormal.isRestricao());
        response.setEmpregoFormal(p1b.getEmpregoFormal());
        response.setScore(p1b.getScore());

        //salvar dados retornados do spc
        utils.salvarSpc(response);

        //se reprovada retornar exceção
        if (status.equals(Status.REPROVADA)) {
            LocalDate dataUltimaConsulta = resultadoConsulta.getConsultaRealizada().getResumo().getDataUltimaOcorrencia()
                    != null ? resultadoConsulta.getConsultaRealizada().getResumo().getDataUltimaOcorrencia().toGregorianCalendar().
                    toZonedDateTime().toLocalDate() : LocalDate.now();
            LocalDate proximo = dataUltimaConsulta.plusDays(90);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String proximoFormatado = proximo.format(formatter);
            throw new PropostaInvalidaException("Ops! Tentar novamente em: " + proximoFormatado);
        }

        //retornar response dos dados do spc
        return response;
    }

    public ComplementoPropostaResponse dadosComplementares(ComplementoPropostaRequest request, Boolean analiseMesaCredito) throws PropostaInvalidaException {
        Boolean enviarAnalise = false;
        String statusValidacaoP2 = "REPROVADO";

        //atualiza dados da proposta enviada
        Proposta proposta = utils.preencherPropostaP2(request, analiseMesaCredito);

        if (proposta.getQdeConsultaUltimosDoisDias() > 2) {
            throw new PropostaInvalidaException(String.format("Cpf consultado %s vezes nos ultimos dois dias", proposta.getQdeConsultaUltimosDoisDias()));
        }

        //converte dados do certiface enviado
        Certiface certiface = certifaceMapper.fromResource(request.getCertiface());
        proposta.setCertiface(certiface);

        //converte dados do cliente enviado
        Cliente cliente = clienteMapper.fromResource(request.getCliente());
        proposta.setCliente(cliente);

        if (analiseMesaCredito) {

            //Prevencao fraude
            PrevencaoFraudeRequest prevencaoFraudeRequest = this.returnPrevencoFraudeRequest(request);
            PrevencaoFraudeResponse prevencaoFraudeResponse = integracaoClient.prevencaoFraude(prevencaoFraudeRequest);
            PrevencaoFraude prevencaoFraude = prevencaoFraudeMapper.fromResource(prevencaoFraudeResponse);
            proposta.setPrevencaoFraude(prevencaoFraude);

            //verifica qual a sugestão de score e aprovação do motor anti-fraudes

            if (!prevencaoFraude.getSugestao().equalsIgnoreCase("Aprovar")) {
                enviarAnalise = true;
            }

            int pontosTotal = 0; //iniciar com zeros de pontos

            //busca dados do SPC
            Spc responseP1 = Spc.buscarPorPropostaId(request.getPropostaId());

            //validar CEP se é o mesmo do SPC comparando com o request (informado pelo cliente)
            Integer pontosCEPconfere = utils.obterPontosCEPConfere(request, responseP1);

            //validar se emprego formal confere com SPC
            Integer pontosEmpregoFormalconfere = utils.obterPontosEmpregoFormalConfere(request, responseP1);

            System.out.println("formal " + pontosEmpregoFormalconfere);

            //validar se nome informado confere com SPC
            Integer pontosNomeconfere = utils.obterPontosNomeConfere(request, responseP1);

            System.out.println("nomeconfere " + pontosNomeconfere);

            //validar se data de nascimento informado confere com SPC
            Integer pontosDataNascimentoconfere = utils.obterPontosDataNascimentoConfere(request, responseP1);

            System.out.println("pontosdatanascimento " + pontosDataNascimentoconfere);

            //caso o bureau informe o telefone e seja igual ao informado na tela (1) senao (0)
            Integer pontosFone = utils.obterPontosTelefone(request, responseP1);

            System.out.println("pontosFone " + pontosFone);

            //caso renda informada confere com Renda presumida considerando margem de tolerância de 30%  (1)  senão (0)
            Integer pontosRenda = utils.obterPontosRenda(request, responseP1);

            System.out.println("pontosRenda " + pontosRenda);

            pontosTotal = pontosFone + pontosRenda + pontosCEPconfere
                    + pontosEmpregoFormalconfere + pontosNomeconfere + pontosDataNascimentoconfere;

            System.out.println("pontosTotal " + pontosTotal);

            if (pontosTotal < 3) {
                enviarAnalise = true;
            }

        }

        //caso o resultado da certiface esteja reprovado, reprovar também a proposta
        if (certiface.getResultado().equalsIgnoreCase("NEGADO")) {
            proposta.setStatus(Status.REPROVADA);
            proposta.setDataReprovacao(LocalDateTime.now());
            proposta.update();
            utils.persistirMotivoStatus(Status.REPROVADA, request.getPropostaId(), certiface.getStatus() + " - " + certiface.getResultado(), proposta.getCpf());
        } else {

            //verificar se precisa submeter proposta para analise
            if (!enviarAnalise) {

                //montar request para submeter analise P2
                P2Request p2Request = utils.obterP2Request(request);

                //caso não seja reprovado pelo motor gerar motivos
                if (!motorClient.validarPropostaP2(p2Request)) {
                    proposta.setStatus(Status.REPROVADA);
                    proposta.setDataReprovacao(LocalDateTime.now());
                    proposta.update();
                    utils.obterMotivoComplementarProposta(request, Status.REPROVADA, request.getPropostaId(), proposta.getCpf());

                    if (!analiseMesaCredito) {
                        utils.persistirMotivoStatus(Status.REPROVADA, request.getPropostaId(), request.getMotivo(), proposta.getCpf());
                    }
                }

                //verifica motor com valor para o limite de crédito
                ClientePropostaRequest clientePropostaRequest = this.gerarLimiteCredito(request.getCliente(), proposta.getScore(), proposta.getEmpregoFormal(), proposta.getRendaInformada());
                clientePropostaRequest.setProfissao(proposta.getProfissao());
                clientePropostaRequest.setRendaInformada(proposta.getRendaInformada());
                clientePropostaRequest.setDiaVencimento(proposta.getDiaVencimento());
                clientePropostaRequest.setPropostaId(proposta.getId().toString());
                proposta.setLimiteCredito(clientePropostaRequest.getLimiteCredito());

                clientePropostaRequest.setAtendente(funcionarioMapper.toResource(proposta.getAtendente()));
                clientePropostaRequest.setLoja(empresaMapper.toResource(proposta.getLoja()));

                proposta.setDataAprovacao(LocalDateTime.now());
                proposta.setStatus(Status.APROVADA);

                proposta.update();
                statusValidacaoP2 = "APROVADO";

                if (!analiseMesaCredito) {
                    utils.persistirMotivoStatus(Status.APROVADA, request.getPropostaId(), request.getMotivo(), proposta.getCpf());
                }
                //envia mensagem para cliente
                utils.enviaSMS(
                        "Uhuuu! Parabéns! Proposta TeuCard aprovada! Procure o atendimento para assinar o contrato!",
                        cliente.getCelular()
                );
                //chama autorizador para criar o cartao caso proposta seja aprovada automaticamente
                autorizadorClient.criarCartao("1111", clientePropostaRequest);


            } else {

                //colocar proposta em analise
                proposta.setStatus(Status.PENDENTE);
                proposta.setDataAnalise(LocalDateTime.now());
                statusValidacaoP2 = "PENDENTE";
                proposta.update();

                if (!analiseMesaCredito) {
                    utils.persistirMotivoStatus(Status.PENDENTE, request.getPropostaId(), request.getMotivo(), proposta.getCpf());
                }
            }

            //persitir motivo status
            utils.persistirMotivoStatus(proposta.getStatus(), proposta.getId().toString(), request.getMotivo(), request.getCliente().getCpf());

        }

        Integer diaVencimento = Integer.parseInt(request.getCliente().getDiaVencimento());
        LocalDate dataMelhorDiaCompra = LocalDate.of(
                LocalDate.now().getYear(),
                LocalDate.now().getMonth(),
                diaVencimento).minus(1, ChronoUnit.DAYS);
        Integer melhorDiaCompra = dataMelhorDiaCompra.getDayOfMonth();

        //cria o response da proposta
        ComplementoPropostaResponse response = new ComplementoPropostaResponse();
        response.setAprovado(statusValidacaoP2);
        response.setPropostaId(request.getPropostaId());
        response.setDiaVencimento(request.getCliente().getDiaVencimento());
        response.setEmail(request.getCliente().getEmail());
        response.setMelhorDiaCompra(melhorDiaCompra);
        response.setLimiteCredito(proposta.getLimiteCredito());
        response.setNomeCliente(request.getCliente().getNome());

        return response;
    }

    public PrevencaoFraudeRequest returnPrevencoFraudeRequest(ComplementoPropostaRequest request) {
        PrevencaoFraudeRequest prevencaoFraudeRequest = new PrevencaoFraudeRequest();
        ClienteRequest clienteRequest = new ClienteRequest();
        PessoaFisicaFraudeRequest pessoaFisica = new PessoaFisicaFraudeRequest();

        List<DocumentosFraudeRequest> documentosFraudeRequestList = new ArrayList<>();
        DocumentosFraudeRequest documentosFraudeRequest = new DocumentosFraudeRequest();

        List<TelefonesFraudeRequest> telefonesFraudeList = new ArrayList<>();
        TelefonesFraudeRequest telefonesFraudeRequest = new TelefonesFraudeRequest();

        List<EnderecosFraudeRequest> enderecosFraudeRequestList = new ArrayList<>();
        EnderecosFraudeRequest enderecosFraudeRequest = new EnderecosFraudeRequest();

        pessoaFisica.setNome(request.getCliente().getNome());
        //pessoaFisica.setSobrenome(request.getCliente());
        pessoaFisica.setDataNascimento(request.getCliente().getDataNascimento().toString());
        pessoaFisica.setSexo(request.getCliente().getGenero());

        documentosFraudeRequest.setNumero(request.getCliente().getCpf());
        documentosFraudeRequest.setTipo("CPF");
        documentosFraudeRequestList.add(documentosFraudeRequest);

        telefonesFraudeRequest.setDdi(55);
        telefonesFraudeRequest.setDdd(Integer.parseInt(request.getCliente().getCelular().substring(0, 2)));
        telefonesFraudeRequest.setNumero(request.getCliente().getCelular().substring(2));
        telefonesFraudeRequest.setTipo("CELULAR");
        telefonesFraudeList.add(telefonesFraudeRequest);

        enderecosFraudeRequest.setLogradouro(request.getCliente().getEndereco().getLogradouro());
        enderecosFraudeRequest.setNumero(request.getCliente().getEndereco().getNumero());
        enderecosFraudeRequest.setCompl1(request.getCliente().getEndereco().getComplemento());
        enderecosFraudeRequest.setCidade(request.getCliente().getEndereco().getCidade());
        enderecosFraudeRequest.setUf(request.getCliente().getEndereco().getUf());
        enderecosFraudeRequest.setCep(request.getCliente().getEndereco().getCep());
        enderecosFraudeRequest.setPais(request.getCliente().getEndereco().getPais());
        //enderecosFraudeRequest.setTipo(request.getCliente().getEndereco().());
        enderecosFraudeRequestList.add(enderecosFraudeRequest);

        clienteRequest.setPessoaFisica(pessoaFisica);
        clienteRequest.setTelefones(telefonesFraudeList);
        clienteRequest.setEnderecos(enderecosFraudeRequestList);
        clienteRequest.setEmail(request.getCliente().getEmail());
        clienteRequest.setDocumentos(documentosFraudeRequestList);

        prevencaoFraudeRequest.setNumeroPedido(request.getPropostaId());
        prevencaoFraudeRequest.setData(LocalDateTime.now().toString());
        prevencaoFraudeRequest.setIdentificacaoEntidade(identificacaoIdentidadeFraude);

        prevencaoFraudeRequest.setCliente(clienteRequest);

        return prevencaoFraudeRequest;
    }


    public byte[] criarPdf(PropostaCartaoRequest propostaCartaoRequest) throws IOException, DocumentException {
        String DEST = "/home/opah/propostaPdf/assinaturaPdf/proposta.pdf";

        File file = new File(DEST);
        file.getParentFile().mkdirs();
        com.itextpdf.text.Document document = new Document();
        document.setMargins(78f, 78f, 78f, 78f);
        PdfWriter.getInstance(document, new FileOutputStream(DEST));

        Proposta proposta = Proposta.findById(new ObjectId(propostaCartaoRequest.getId()));
        document.open();


        String tittleDocument = "CONTRATO DE EMISSÃO E USO DO CARTÃO TEUCARD PRIVATE LABEL\n\n\n";
        com.itextpdf.text.Paragraph tittleDOcumentParagraph = new com.itextpdf.text.Paragraph(tittleDocument);
        tittleDOcumentParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(tittleDOcumentParagraph);

        String parte1 = "EMISSOR: TEUCARD instituição sediada Av Torquato Tapajós, no 2871 / CEP: 69.048-\n\n" +
                "010, Manaus/AM, empresa responsável pela emissão e administração dos produtos\n" +
                "Cartão TeuCard (“CARTÃO”).\n\n" +
                "PORTADOR TITULAR: " + proposta.getCliente().getNome() + ".\n\n" +
                "O Cliente ao assinar o termo de adesão de seu CARTÃO, desbloquear e utilizar, estará" +
                "concordando com todos os termos e condições deste contrato de utilização do seu" +
                "CARTÃO TEUCARD, dessa forma, o contrato deve ser lido atentamente as condições" +
                "gerais aplicáveis ao seu Cartão TeuCard, onde consta os direitos e obrigações do" +
                "Cliente Beneficiário. Em caso de dúvidas, você poderá entrar em contato com a Central" +
                "de Atendimento nos números Central de atendimento: 4003-3203 Whatsapp: 92" +
                "4003-3203; - E-mail: sac@useteucard.com.br; - Site: http://useteucard.com.br/";
        com.itextpdf.text.Paragraph parte1Paragraph = new com.itextpdf.text.Paragraph(parte1);
        parte1Paragraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(parte1Paragraph);


        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(400);
        table.setLockedWidth(true);
        table.setWidths(new int[]{1, 100});
        table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cell;
        cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.addElement(new com.itextpdf.text.Paragraph(""));
        table.addCell(cell);
        cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        com.itextpdf.text.List list = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);

        String espaçoTab = "\u00A0\u00A0\u00A0\u00A0\u00A0\u00A0";

        com.itextpdf.text.Paragraph tittleDefinicoes = new com.itextpdf.text.Paragraph(espaçoTab + "1. DAS DEFINIÇÕES");
        document.add(tittleDefinicoes);

        ListItem itema = new ListItem(new Chunk("\na. Para facilitar seu entendimento, seguem abaixo os significados dos" +
                "principais termos mencionados no presente;"));
        itema.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem itemb = new ListItem(new Chunk("b. CARTÃO – Plástico que contém o número, características de segurança," +
                "prazo de validade, tarja magnética e identificação da logomarca ou" +
                "marca d`água TeuCard, utilizado como meio de pagamento de bens e/ou" +
                "serviços, bem como para outras operações descritas neste Contrato," +
                "conforme características específicas do CARTÃO por você contratado;"));
        itemb.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem itemc = new ListItem(new Chunk("c. ENCARGOS – São os juros e tributos devidos quando houver" +
                "financiamento das despesas realizadas com o CARTÃO ou contratação" +
                "de operações de financiamentos;"));
        itemc.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem itemd = new ListItem(new Chunk("d. FATURA – Documento emitido mensalmente quando realizadas" +
                "transações ou quando houver quaisquer cobranças realizadas no cartão." +
                "A fatura conterá informações acerca do valor total das despesas" +
                "realizadas com seu CARTÃO, LIMITE DE CRÉDITO, PAGAMENTO MÍNIMO," +
                "e demais informações constantes na cláusula 10 deste Contrato;"));
        itemd.setAlignment(Element.ALIGN_JUSTIFIED);


        ListItem iteme = new ListItem(new Chunk("e. JUROS MORATÓRIOS – É uma taxa percentual aplicada em razão do" +
                "atraso no pagamento."));
        iteme.setAlignment(Element.ALIGN_JUSTIFIED);


        ListItem itemf = new ListItem(new Chunk("f. LIMITE DE CRÉDITO – Valor máximo estabelecido pelo EMISSOR, para" +
                "realização de transações com o CARTÃO nos estabelecimentos" +
                "credenciados. Engloba seu limite para compras;"));
        itemf.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem itemg = new ListItem(new Chunk("g. PAGAMENTO MÍNIMO – Valor, indicado na FATURA, para pagamento" +
                "das despesas até a data de vencimento da fatura, para que não seja" +
                "caracterizada a inadimplência."));
        itemg.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem itemh = new ListItem(new Chunk("h. PORTADOR TITULAR – Pessoa física contratante do CARTÃO autorizada" +
                "a realizar TRANSAÇÕES, que deverá ser maior, capaz e não poderá" +
                "possuir qualquer restrição junto a Instituições Financeiras, Centrais de" +
                "Informações de Crédito e similares. O PORTADOR TITULAR é o" +
                "responsável pelo integral cumprimento das obrigações aqui assumidas," +
                "em especial pelo pagamento da FATURA onde são lançadas as DESPESAS."));
        itemh.setAlignment(Element.ALIGN_JUSTIFIED);


        ListItem itemi = new ListItem(new Chunk("i. SENHA ELETRÔNICA – Código pessoal e intransferível que constitui sua" +
                "identificação e assinatura, quando da realização de transações nos" +
                "ESTABELECIMENTOS CREDENCIADOS;"));
        itemi.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem itemk = new ListItem(new Chunk("k. Todas as informações dos dados e limite do Portador do Cartão TeuCard."));
        itemk.setAlignment(Element.ALIGN_JUSTIFIED);


        //add to list
        list.add(itema);
        list.add(itemb);
        list.add(itemc);
        list.add(itemd);
        list.add(iteme);
        list.add(itemf);
        list.add(itemg);
        list.add(itemh);
        list.add(itemi);
        list.add(itemk);


        cell.addElement(list);
        table.addCell(cell);
        document.add(table);


        //SEGUNDA PARTE


        PdfPTable table2 = new PdfPTable(2);
        table2.setTotalWidth(400);
        table2.setLockedWidth(true);
        table2.setWidths(new int[]{1, 100});
        table2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cell2;
        cell2 = new PdfPCell();
        cell2.setBorder(PdfPCell.NO_BORDER);
        cell2.addElement(new com.itextpdf.text.Paragraph(""));
        table2.addCell(cell2);
        cell2 = new PdfPCell();
        cell2.setBorder(PdfPCell.NO_BORDER);
        com.itextpdf.text.List list2 = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);

        //segunda parte
        com.itextpdf.text.Paragraph tittleAdesaoTeuCard = new com.itextpdf.text.Paragraph(espaçoTab + "2. DA ADESÃO AO CONTRATO CARTÃO TeuCard.");
        document.add(tittleAdesaoTeuCard);

        ListItem itema2 = new ListItem(new Chunk("\na. A emissão do CARTÃO depende da aprovação pelo Emissor, segundo" +
                "seus critérios exclusivos de análise cadastral e creditícia."));
        itema2.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem itemb2 = new ListItem(new Chunk("b. O CARTÃO é de propriedade do EMISSOR e deverá ser devolvido a este" +
                "quando solicitado."));
        itemb2.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem itemc2 = new ListItem(new Chunk("c. O titular do cartão concretizará a adesão ao CARTÃO TeuCard quando" +
                "qualquer um dos eventos abaixo ocorrer:"));
        itemc2.setAlignment(Element.ALIGN_JUSTIFIED);

        //todo sublist

        PdfPTable table3 = new PdfPTable(2);
        table3.setTotalWidth(400);
        table3.setLockedWidth(true);
        table3.setWidths(new int[]{1, 100});
        table3.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cell3;
        cell3 = new PdfPCell();
        cell3.setBorder(PdfPCell.NO_BORDER);
        cell3.addElement(new com.itextpdf.text.Paragraph(""));
        table3.addCell(cell3);
        cell3 = new PdfPCell();
        cell3.setBorder(PdfPCell.NO_BORDER);
        com.itextpdf.text.List list3 = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);


        ListItem subitem1 = new ListItem(new Chunk("\ni. assinatura do termo de adesão que poderá ser feita" +
                "pessoalmente nas LOJAS Nova Era ou por meio de confirmação" +
                "via Internet. Você responderá pela veracidade das informações" +
                "prestadas nestes canais;"));
        subitem1.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem subitem2 = new ListItem(new Chunk("ii. desbloqueio dos CARTÕES junto aos canais de digitais;" +
                "prestadas nestes canais;"));
        subitem2.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem subitem3 = new ListItem(new Chunk("iii. utilização dos CARTÕES mediante o uso de SENHA ELETRÔNICA;"));
        subitem3.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem subitem4 = new ListItem(new Chunk("iv. a adesão ao CARTÃO implica em plena concordância com as" +
                "cláusulas do presente contrato;"));
        subitem4.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem subitem5 = new ListItem(new Chunk("v. seu CARTÃO é nominativo, intransferível, de uso pessoal e" +
                "exclusivo. Em caso de falecimento do titular, o CARTÃO não mais" +
                "poderá ser utilizado, devendo seus sucessores ou Espólio" +
                "comunicar o Emissor, no menor prazo possível, para promover" +
                "seu cancelamento."));
        subitem5.setAlignment(Element.ALIGN_JUSTIFIED);


        //add to list2
        list2.add(itema2);
        list2.add(itemb2);
        list2.add(itemc2);

        //add sublist 3
        list3.add(subitem1);
        list3.add(subitem2);
        list3.add(subitem3);
        list3.add(subitem4);
        list3.add(subitem5);


        cell2.addElement(list2);
        table2.addCell(cell2);

        cell3.addElement(list3);
        table3.addCell(cell3);

        document.add(table2);
        document.add(table3);

        //parte 3


        PdfPTable table4 = new PdfPTable(2);
        table4.setTotalWidth(400);
        table4.setLockedWidth(true);
        table4.setWidths(new int[]{1, 100});
        table4.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cell4;
        cell4 = new PdfPCell();
        cell4.setBorder(PdfPCell.NO_BORDER);
        cell4.addElement(new com.itextpdf.text.Paragraph(""));
        table4.addCell(cell4);
        cell4 = new PdfPCell();
        cell4.setBorder(PdfPCell.NO_BORDER);
        com.itextpdf.text.List list4 = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);

        //segunda parte
        com.itextpdf.text.Paragraph tittleUsoDoCartao = new com.itextpdf.text.Paragraph(espaçoTab + "3. DO USO DO CARTÃO");
        document.add(tittleUsoDoCartao);

        ListItem itema3 = new ListItem(new Chunk("\na. O CARTÃO é utilizado como meio de pagamento para compras de bens" +
                "e/ou serviços a ser utilizado apenas nas LOJAS do GRUPO NOVA ERA."));
        itema3.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem itemb3 = new ListItem(new Chunk("b. O titular do cartão deverá confirmar a transação com seu CARTÃO" +
                "através do registro de sua SENHA ELETRÔNICA. TAIS PROCEDIMENTOS" +
                "REPRESENTARÃO MANIFESTAÇÃO DE VONTADE INEQUÍVOCA, CIÊNCIA," +
                "RESPONSABILIDADE E ACEITAÇÃO DA TRANSAÇÃO, mediante a senha" +
                "única e intransferível."));
        itemb3.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem itemc3 = new ListItem(new Chunk("c. O EMISSOR não se responsabilizará pelo preço, qualidade e quantidade" +
                "dos bens/serviços adquiridos/contratados através de transações nos" +
                "CARTÕES. Qualquer pendência relacionada a referidos produtos ou" +
                "serviços deverá ser resolvida junto ao ESTABELECIMENTO" +
                "CREDENCIADO."));
        itemc3.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem itemd3 = new ListItem(new Chunk("d. É vedada a utilização de seu CARTÃO para a prática de jogos de azar, ou" +
                "realização de compras de produtos ilegais, sem prejuízo das penalidades" +
                "civis e criminais aplicáveis, conforme legislação em vigor. Caso isso" +
                "ocorra, o CLIENTE será o único responsável por qualquer consequência" +
                "decorrente da utilização do CARTÃO, inclusive por terceiros, salvo se" +
                "comprovada a existência de fraude na utilização do CARTÃO, que deverá" +
                "ser analisada pelo Emissor."));
        itemd3.setAlignment(Element.ALIGN_JUSTIFIED);


        ListItem iteme3 = new ListItem(new Chunk("e. O titular do cartão deverá zelar pela segurança de informações" +
                "transmitidas através de acesso via Internet, principalmente envolvendo" +
                "transações mediante a utilização do número do CARTÃO."));
        iteme3.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem itemf3 = new ListItem(new Chunk("f. A SENHA ELETRÔNICA é a assinatura eletrônica pessoal, intransferível e" +
                "confidencial. O cliente não deverá informar sua SENHA ELETRÔNICA a" +
                "qualquer pessoa, por mais privilegiada que seja, sob pena de rescisão," +
                "bem como responsabilização pela utilização do CARTÃO.\n" +
                "i. A SENHA ELETRÔNICA também poderá ser utilizada para\n" +
                "consultas de valores, saldos, extrato."));
        itemf3.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem itemg3 = new ListItem(new Chunk("g. O EMISSOR poderá autorizar, a seu critério, novas formas de utilização" +
                "do CARTÃO, estando expressamente proibida, todavia, sua utilização por" +
                "terceiros, de maneira não prevista neste CONTRATO ou em" +
                "estabelecimentos não credenciados."));
        itemg3.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem itemh3 = new ListItem(new Chunk("h. Em caso de roubo, furto ou extravio de sua senha, titular do cartão" +
                "poderá registrar nova Senha Eletrônica nos canais eletrônicos, seguindo" +
                "as orientações do sistema."));
        itemh3.setAlignment(Element.ALIGN_JUSTIFIED);

        list4.add(itema3);
        list4.add(itemb3);
        list4.add(itemc3);
        list4.add(itemd3);
        list4.add(iteme3);
        list4.add(itemf3);
        list4.add(itemg3);
        list4.add(itemh3);
        cell4.addElement(list4);
        table4.addCell(cell4);
        document.add(table4);


        //parte 4


        PdfPTable table5 = new PdfPTable(2);
        table5.setTotalWidth(400);
        table5.setLockedWidth(true);
        table5.setWidths(new int[]{1, 100});
        table5.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cell5;
        cell5 = new PdfPCell();
        cell5.setBorder(PdfPCell.NO_BORDER);
        cell5.addElement(new com.itextpdf.text.Paragraph(""));
        table5.addCell(cell5);
        cell5 = new PdfPCell();
        cell5.setBorder(PdfPCell.NO_BORDER);
        com.itextpdf.text.List list5 = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);


        com.itextpdf.text.Paragraph tittleParcelamentoDespesas = new com.itextpdf.text.Paragraph(espaçoTab + "4. DAS FORMAS DE PARCELAMENTO DAS DESPESAS");
        document.add(tittleParcelamentoDespesas);

        ListItem itema4 = new ListItem(new Chunk("\na. Quando realizar compras ou operações de crédito com seu CARTÃO," +
                "titular do cartão poderá efetuar o pagamento de suas DESPESAS à vistaou de forma parcelada. O parcelamento de suas despesas se dará" +
                "através das seguintes modalidades:" +
                "\ni. Parcelado sem Juros; e, (ii) Parcelado com Juros." +
                "\nii. Parcelado sem Juros:" +
                "1. Corresponde à possibilidade de parcelamento das" +
                "despesas oferecida pelo estabelecimento quando da" +
                "utilização do CARTÃO. As parcelas serão lançadas" +
                "diretamente em sua fatura." +
                "2. A somatória de todas as parcelas decorrentes da utilização" +
                "do CARTÃO na modalidade Parcelado sem Juros, será" +
                "incluída no cômputo do LIMITE DE CRÉDITO, e a" +
                "recomposição do limite de crédito ocorrerá" +
                "proporcionalmente, e somente após, compensado o" +
                "pagamento de cada parcela." +
                "\niii. Parcelado com Juros" +
                "\n1. Corresponde ao parcelamento das despesas através de" +
                "FINANCIAMENTO obtido junto ao EMISSOR, sob o qual" +
                "incidirá taxa de juros remuneratórios pré-fixada." +
                "\n2. As parcelas das DESPESAS serão iguais, mensais," +
                "consecutivas e descritas na sua FATURA, e, os juros" +
                "remuneratórios serão contados a partir da data da" +
                "TRANSAÇÃO até a data do vencimento de cada parcela e" +
                "serão cobrados juntamente com as DESPESAS na FATURA." +
                "\n3. O EMISSOR poderá, mediante prévia comunicação," +
                "estabelecer condições específicas de pagamento das" +
                "parcelas do Parcelado com Juros, inclusive podendo" +
                "postergar a data de vencimento de algumas parcelas." +
                "\n4. Fica estabelecido, ainda, que a somatória de todas as" +
                "parcelas referentes ao pagamento na modalidade" +
                "Parcelado com Juros será incluída no cômputo do LIMITE" +
                "DE CRÉDITO e a recomposição do limite de crédito" +
                "ocorrerá proporcionalmente e somente após compensado" +
                "o pagamento de cada parcela."));
        itema4.setAlignment(Element.ALIGN_JUSTIFIED);


        list5.add(itema4);

        cell5.addElement(list5);
        table5.addCell(cell5);
        document.add(table5);


        //parte 5


        PdfPTable table6 = new PdfPTable(2);
        table6.setTotalWidth(400);
        table6.setLockedWidth(true);
        table6.setWidths(new int[]{1, 100});
        table6.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cell6;
        cell6 = new PdfPCell();
        cell6.setBorder(PdfPCell.NO_BORDER);
        cell6.addElement(new com.itextpdf.text.Paragraph(""));
        table6.addCell(cell6);
        cell6 = new PdfPCell();
        cell6.setBorder(PdfPCell.NO_BORDER);
        com.itextpdf.text.List list6 = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);


        com.itextpdf.text.Paragraph tittleLiquidacaoAntecipada = new com.itextpdf.text.Paragraph(espaçoTab + "5. DA LIQUIDAÇÃO ANTECIPADA");
        document.add(tittleLiquidacaoAntecipada);

        ListItem itema5 = new ListItem(new Chunk("\na. O titular do cartão poderá efetuar o pagamento antecipado de parcelas" +
                "a vencer mediante redução proporcional dos encargos incidentes. A" +
                "solicitação de antecipação deve ser realizada nos canais de atendimento"));
        itema5.setAlignment(Element.ALIGN_JUSTIFIED);

        ListItem itema6 = new ListItem(new Chunk("b. Em caso de liquidação antecipada de operações, a taxa utilizada para" +
                "apuração do valor presente será a taxa pactuada na contratação da" +
                "operação."));
        itema6.setAlignment(Element.ALIGN_JUSTIFIED);


        list6.add(itema5);
        list6.add(itema6);

        cell6.addElement(list6);
        table6.addCell(cell6);
        document.add(table6);


        //parte 6


        PdfPTable table7 = new PdfPTable(2);
        table7.setTotalWidth(400);
        table7.setLockedWidth(true);
        table7.setWidths(new int[]{1, 100});
        table7.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cell7;
        cell7 = new PdfPCell();
        cell7.setBorder(PdfPCell.NO_BORDER);
        cell7.addElement(new com.itextpdf.text.Paragraph(""));
        table7.addCell(cell7);
        cell7 = new PdfPCell();
        cell7.setBorder(PdfPCell.NO_BORDER);
        com.itextpdf.text.List list7 = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);


        com.itextpdf.text.Paragraph tittleDoCartao = new com.itextpdf.text.Paragraph(espaçoTab + "6. DO CARTÃO");
        document.add(tittleDoCartao);

        ListItem itema7 = new ListItem(new Chunk("\na. TARIFAS" +
                "\ni. TARIFA DE ANUIDADE - Poderá ser cobrada TARIFA DE ANUIDADE" +
                "em sua FATURA, a partir da adesão ao CARTÃO. A TARIFA DE" +
                "ANUIDADE visa remunerar o EMISSOR pela prestação de serviços" +
                "consistentes em disponibilizar e manter o CARTÃO TeuCard." +
                "\nii. O titular do cartão será previamente informado caso referido" +
                "valor seja reajustado pelo EMISSOR, nos termos da legislação em" +
                "vigor." +
                "\niii. Caso o titular do cartão não concorde com a cobrança da TARIFA" +
                "DE ANUIDADE, poderá rescindir o presente CONTRATO. A" +
                "renúncia do EMISSOR à cobrança da tarifa prevista nesta cláusula" +
                "representará mera liberalidade e não importará em renúncia a" +
                "outras parcelas ou tarifas devidas." +
                "\nb. DEMAIS TARIFAS" +
                "\ni. Poderá ainda o EMISSOR realizar a cobrança de outras tarifas por" +
                "outros serviços que serão disponibilizados aos PORTADORES por" +
                "intermédio do CARTÃO, sendo certo que tais tarifas serão" +
                "divulgadas previamente de forma a respeitar a legislação e/ou" +
                "regulamentação vigente."));
        itema7.setAlignment(Element.ALIGN_JUSTIFIED);

        list7.add(itema7);
        cell7.addElement(list7);
        table7.addCell(cell7);
        document.add(table7);


        //parte 7


        PdfPTable table8 = new PdfPTable(2);
        table8.setTotalWidth(400);
        table8.setLockedWidth(true);
        table8.setWidths(new int[]{1, 100});
        table8.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cell8;
        cell8 = new PdfPCell();
        cell8.setBorder(PdfPCell.NO_BORDER);
        cell8.addElement(new com.itextpdf.text.Paragraph(""));
        table8.addCell(cell8);
        cell8 = new PdfPCell();
        cell8.setBorder(PdfPCell.NO_BORDER);
        com.itextpdf.text.List list8 = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);


        com.itextpdf.text.Paragraph tittleDaFatura = new com.itextpdf.text.Paragraph(espaçoTab + "7. DA FATURA");
        document.add(tittleDaFatura);

        ListItem itema8 = new ListItem(new Chunk("\na. O titular do cartão terá acesso a FATURA somente quando incorrer em" +
                "DESPESAS, na qual constarão as seguintes informações:" +
                "\ni. a identificação do CARTÃO;" +
                "\nii. o LIMITE DE CRÉDITO, estes dois últimos quando disponíveis;" +
                "\niii. a data de vencimento para pagamento das despesas;" +
                "\niv. a descrição das transações realizadas no seu CARTÃO;" +
                "\nv. o valor do PAGAMENTO MÍNIMO;" +
                "\nvi. CET – Custo Efetivo Total, conforme legislação em vigor; e" +
                "\nvii. o valor total das despesas para pagamento, considerando:" +
                "\nb. O titular do cartão deverá efetuar o pagamento da FATURA na data" +
                "indicada. Para tanto, você terá as opções de:" +
                "\ni. Pagar o valor total da fatura; ou" +
                "\nii. Pagar qualquer valor entre o Pagamento Mínimo e o valor total" +
                "da sua fatura, financiando o saldo devedor. Nesse caso, referido" +
                "saldo devedor poderá ser objeto de financiamento na modalidade" +
                "de crédito rotativo até o vencimento da fatura subsequente." +
                "\nc. Caso o titular do cartão não receba a FATURA com 3 (três) dias de" +
                "antecedência à data do vencimento, deverá entrar em contato com a" +
                "Central de Relacionamento para verificar o valor devido e o" +
                "procedimento para o pagamento desses valores." +
                "\nd. Verificada qualquer dúvida com relação às despesas constantes em sua" +
                "FATURA, o titular do cartão deverá contatar o EMISSOR através dos" +
                "canais de atendimento, para prestação de todas as informações" +
                "necessárias e providências cabíveis, conforme o caso." +
                "\ne.O titular do cartão acessará a sua FATURA por meio digitais." +
                "\nf. A FATURA ainda poderá ser utilizada para comunicação prévia de" +
                "\ni. eventuais cobranças de novas tarifas ou aumento; (ii) alterações" +
                "nas condições do CONTRATO; e" +
                "\nii. promoções ou informações de seu interesse."));
        itema8.setAlignment(Element.ALIGN_JUSTIFIED);

        list8.add(itema8);
        cell8.addElement(list8);
        table8.addCell(cell8);
        document.add(table8);


        //parte 8


        PdfPTable table9 = new PdfPTable(2);
        table9.setTotalWidth(400);
        table9.setLockedWidth(true);
        table9.setWidths(new int[]{1, 100});
        table9.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cell9;
        cell9 = new PdfPCell();
        cell9.setBorder(PdfPCell.NO_BORDER);
        cell9.addElement(new com.itextpdf.text.Paragraph(""));
        table9.addCell(cell9);
        cell9 = new PdfPCell();
        cell9.setBorder(PdfPCell.NO_BORDER);
        com.itextpdf.text.List list9 = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);


        com.itextpdf.text.Paragraph tittleConstetacoesDespesa = new com.itextpdf.text.Paragraph(espaçoTab + "8. DAS CONTESTAÇÕES DE DESPESAS");
        document.add(tittleConstetacoesDespesa);

        ListItem itema9 = new ListItem(new Chunk("\na. O titular do cartão poderá contestar qualquer TRANSAÇÃO e/ou" +
                "DESPESAS constantes na FATURA, no prazo de até 30 (trinta) dias a" +
                "contar da data do vencimento da respectiva FATURA. Para tanto deverá" +
                "apresentar a contestação através dos canais de atendimento" +
                "disponibilizados pelo EMISSOR." +
                "\nb. Ocorrendo a apresentação da contestação, o EMISSOR poderá, sem que" +
                "tal procedimento constitua assunção de nova dívida, desconsiderar a" +
                "TRANSAÇÃO e/ou DESPESA contestada no momento do pagamento da" +
                "FATURA, ou estorná-la, na hipótese da TRANSAÇÃO e/ou DESPESAS" +
                "contestada já ter sido paga." +
                "\nc. Verificada a improcedência da contestação, o valor da TRANSAÇÃO e/ou" +
                "da DESPESA indevidamente contestada será reconsiderada na FATURA" +
                "subsequente. Sobre tal valor, incidirão os juros remuneratórios e demais" +
                "encargos de inadimplência relativos ao FINANCIAMENTO desde a data" +
                "do vencimento da respectiva FATURA contestada."));
        itema9.setAlignment(Element.ALIGN_JUSTIFIED);

        list9.add(itema9);
        cell9.addElement(list9);
        table9.addCell(cell9);
        document.add(table9);

        //parte 9


        PdfPTable table10 = new PdfPTable(2);
        table10.setTotalWidth(400);
        table10.setLockedWidth(true);
        table10.setWidths(new int[]{1, 100});
        table10.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cell10;
        cell10 = new PdfPCell();
        cell10.setBorder(PdfPCell.NO_BORDER);
        cell10.addElement(new com.itextpdf.text.Paragraph(""));
        table10.addCell(cell10);
        cell10 = new PdfPCell();
        cell10.setBorder(PdfPCell.NO_BORDER);
        com.itextpdf.text.List list10 = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);


        com.itextpdf.text.Paragraph tittlePagamentoDespesa = new com.itextpdf.text.Paragraph(espaçoTab + "9. DO PAGAMENTO DAS DESPESAS");
        document.add(tittlePagamentoDespesa);

        ListItem itema10 = new ListItem(new Chunk("\na. O pagamento das DESPESAS deverá ser realizado na forma e na data" +
                "selecionada pelo titular do cartão quando da adesão ao CARTÃO" +
                "TeuCard."));
        itema10.setAlignment(Element.ALIGN_JUSTIFIED);

        list10.add(itema10);
        cell10.addElement(list10);
        table10.addCell(cell10);
        document.add(table10);


        //parte 10


        PdfPTable table11 = new PdfPTable(2);
        table11.setTotalWidth(400);
        table11.setLockedWidth(true);
        table11.setWidths(new int[]{1, 100});
        table11.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cell11;
        cell11 = new PdfPCell();
        cell11.setBorder(PdfPCell.NO_BORDER);
        cell11.addElement(new com.itextpdf.text.Paragraph(""));
        table11.addCell(cell11);
        cell11 = new PdfPCell();
        cell11.setBorder(PdfPCell.NO_BORDER);
        com.itextpdf.text.List list11 = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);


        com.itextpdf.text.Paragraph tittleDoLimite = new com.itextpdf.text.Paragraph(espaçoTab + "10. DO LIMITE");
        document.add(tittleDoLimite);

        ListItem itema11 = new ListItem(new Chunk("\na. O titular do cartão poderá utilizar seu CARTÃO até o LIMITE DE CRÉDITO" +
                "concedido de acordo com a sua elegibilidade." +
                "\nb. Os LIMITES mencionados serão informados diretamente pelo EMISSOR," +
                "calculados de acordo com a sua política de crédito com base nas" +
                "informações creditícias obtidas, e válidos durante o período de vigência" +
                "do CONTRATO." +
                "\nc. A realização de TRANSAÇÕES reduzirá o montante dos LIMITES" +
                "estipulados, que serão restabelecidos automaticamente após o efetivo" +
                "pagamento das DESPESAS referentes a cada respectivo limite." +
                "\nd. O LIMITE DE CRÉDITO poderá ser elevado ou reduzido, conforme a" +
                "política de crédito do EMISSOR, devendo o cliente ser previamente" +
                "informado, dentro do prazo estabelecido pela legislação vigente, através" +
                "dos canais de comunicação oficiais do Emissor." +
                "\ni. O titular do cartão poderá autorizar o EMISSOR a aumentar o" +
                "seu LIMITE DE CRÉDITO do CARTÃO periodicamente, de acordo" +
                "com a sua política de crédito e a análise de elegibilidade do" +
                "Cliente."));
        itema11.setAlignment(Element.ALIGN_JUSTIFIED);

        list11.add(itema11);
        cell11.addElement(list11);
        table11.addCell(cell11);
        document.add(table11);


        //parte 10


        PdfPTable table12 = new PdfPTable(2);
        table12.setTotalWidth(400);
        table12.setLockedWidth(true);
        table12.setWidths(new int[]{1, 100});
        table12.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cell12;
        cell12 = new PdfPCell();
        cell12.setBorder(PdfPCell.NO_BORDER);
        cell12.addElement(new com.itextpdf.text.Paragraph(""));
        table12.addCell(cell12);
        cell12 = new PdfPCell();
        cell12.setBorder(PdfPCell.NO_BORDER);
        com.itextpdf.text.List list12 = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);


        com.itextpdf.text.Paragraph tittleDoAtraso = new com.itextpdf.text.Paragraph(espaçoTab + "11. DO ATRASO");
        document.add(tittleDoAtraso);

        ListItem itema12 = new ListItem(new Chunk("\na. Na hipótese de não pagamento da FATURA no vencimento e na forma" +
                "pré-estabelecida, poderão incidir os seguintes encargos:" +
                "\ni. multa de 2% (dois por cento) calculada sobre o saldo devedor" +
                "total da fatura;" +
                "\nii. juros remuneratórios indicados na Fatura, mais juros moratórios" +
                "à taxa de 1% (um por cento) ao mês, ambos capitalizados" +
                "diariamente, aplicáveis sobre os valores devidos e não pagos" +
                "desde a data do vencimento até a data do efetivo pagamento; e" +
                "\niii. tributos devidos na forma da legislação em vigor." +
                "\nb. No caso de atraso no pagamento, a taxa percentual correspondente aos" +
                "juros remuneratórios será:" +
                "\ni. a taxa da operação do parcelamento, no caso das operações" +
                "realizadas nos termos do art. 2o, da Resolução no 4.549, de 26 de" +
                "janeiro de 2017; e" +
                "\nii. a taxa de juros da modalidade de crédito rotativo, para os demais" +
                "valores em atraso." +
                "\niii. Caso o titular do cartão realize o pagamento da sua Fatura em" +
                "atraso, você deve consultar nos canais digitais qual o valor" +
                "atualizado do seu saldo devedor (valor total da fatura + multa +" +
                "juros remuneratórios + juros de mora) na data do pagamento. Se" +
                "você optar por pagar valor inferior ao saldo devedor atualizado, adiferença será financiada pelo Emissor, estando sujeita à" +
                "cobrança de Encargos, conforme previsto acima." +
                "\nc. Em caso de atraso ou não pagamento, poderá ter o seu cartão" +
                "bloqueado, bem como o seu nome inscrito no SPC, Serasa e demais" +
                "órgãos de proteção ao crédito." +
                "\nd. Na hipótese de atraso, total ou parcial, em relação aos pagamentos, o" +
                "titular do cartão autoriza, desde já, o EMISSOR a realizar a compensação" +
                "entre os valores devidos ao EMISSOR com os valores que lhe são devidos," +
                "até a quitação integral." +
                "\ni. A compensação parcial não o exonerará quanto ao pagamento do" +
                "saldo remanescente de suas obrigações e respectivos acréscimos," +
                "até a quitação total dos valores devidos decorrentes do presente" +
                "CONTRATO. 13.7. – Desde já reconhece e autoriza que o" +
                "EMISSOR, por meio de seus prepostos, terceiros ou funcionários," +
                "através de contatos telefônicos, correspondências ou quaisquer" +
                "outros meios, entre em contato durante o horário comercial para" +
                "lhe informar eventuais débitos em atraso." +
                "\ne. O EMISSOR colocará as suas tarifas, taxas de juros remuneratórios e" +
                "demais encargos vigentes incluindo, mas não se limitando, o CET – Custo" +
                "Efetivo Total, conforme legislação em vigor, sem prejuízo do disposto."));
        itema12.setAlignment(Element.ALIGN_JUSTIFIED);

        list12.add(itema12);
        cell12.addElement(list12);
        table12.addCell(cell12);
        document.add(table12);


        //parte 11


        PdfPTable table13 = new PdfPTable(2);
        table13.setTotalWidth(400);
        table13.setLockedWidth(true);
        table13.setWidths(new int[]{1, 100});
        table13.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cell13;
        cell13 = new PdfPCell();
        cell13.setBorder(PdfPCell.NO_BORDER);
        cell13.addElement(new com.itextpdf.text.Paragraph(""));
        table13.addCell(cell13);
        cell13 = new PdfPCell();
        cell13.setBorder(PdfPCell.NO_BORDER);
        com.itextpdf.text.List list13 = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);


        com.itextpdf.text.Paragraph tittleDaRescisaoCancelamento = new com.itextpdf.text.Paragraph(espaçoTab + "12. DA RESCISÃO E CANCELAMENTO");
        document.add(tittleDaRescisaoCancelamento);

        ListItem itema13 = new ListItem(new Chunk("\na. Este CONTRATO poderá ser rescindido por qualquer das partes, a" +
                "qualquer tempo, mediante prévia comunicação, sem motivo expresso" +
                "ou conforme disposições legais, com o consequente cancelamento do" +
                "CARTÃO e o encerramento da CONTA CARTÃO." +
                "\nb. O EMISSOR poderá rescindir os efeitos deste CONTRATO em relação" +
                "ao(s) PORTADOR do CARTÃO. A violação de qualquer disposição deste" +
                "CONTRATO dará causa a rescisão imediata deste CONTRATO, sem" +
                "qualquer formalidade adicional, e a suspensão ou o cancelamento dos" +
                "CARTÕES e da CONTA CARTÃO, a critério único e exclusivo do EMISSOR," +
                "de acordo com sua política, sem prejuízo de cobrança dos valores" +
                "devidos, em especial:" +
                "\ni. o não pagamento da FATURA ou do PAGAMENTO MÍNIMO," +
                "\nii. o não pagamento de qualquer das obrigações presentes neste" +
                "CONTRATO, ou de qualquer outra obrigação perante o EMISSOR;" +
                "\niii. a realização de gastos além dos limites estabelecidos, salvo" +
                "autorização do EMISSOR;" +
                "\niv. a inclusão de seu nome em qualquer lista de restrição ao crédito;" +
                "\nv. falta de fundos para cobrir os débitos em conta;c." +
                "\nvi. a realização de qualquer TRANSAÇÃO fraudulenta, atividades" +
                "consideradas ilícitas como crime de lavagem de dinheiro ou" +
                "ocultação de bens, direitos e valores, ou outras vedadas pela" +
                "legislação;" +
                "\nvii. o falecimento do TITULAR;" +
                "\nviii. movimentação incompatível com a capacidade financeira ou" +
                "atividade desenvolvida;" +
                "\nix. utilização de meios não idôneos, com objetivo de postergar" +
                "pagamentos e/ou cumprimento de obrigações assumidas com o" +
                "EMISSOR ou qualquer empresa pertencente ao mesmo grupo" +
                "Nova Era;" +
                "\nx. irregularidade nas informações prestadas, julgadas de natureza" +
                "grave pelo EMISSOR e;" +
                "\nxi. Cadastro de Pessoa Física – CPF/MF que não esteja classificado" +
                "como ativo pela Secretaria da Receita Federal;" +
                "\nc. O cancelamento do CARTÃO não extingue as dívidas vencidas," +
                "vincendas, e as despesas incorridas após a data da rescisão, que deverão" +
                "ser imediatamente quitadas. Todos os benefícios colocados à sua" +
                "disposição serão automaticamente cancelados; e, o pagamento de todas" +
                "as DESPESAS, não implicará a renovação deste CONTRATO, nem ao" +
                "menos a revalidação do CARTÃO." +
                "\nd. Em caso de falecimento do TITULAR, o Espólio responderá pelas" +
                "DESPESAS realizadas através do CARTÃO, inclusive aquelas realizadas" +
                "entre a data do falecimento e a data da sua comunicação." +
                "\ne. A NÃO UTILIZAÇÃO DO CARTÃO, POR UM PERÍODO SUPERIOR A 12" +
                "(DOZE) MESES, COMPUTADOS A PARTIR DA ÚLTIMA TRANSAÇÃO OU" +
                "REGISTRO DA SENHA ELETRÔNICA, PODERÁ IMPLICAR NO" +
                "CANCELAMENTO DOS MESMOS SEM QUALQUER FORMALIDADE" +
                "ADICIONAL, DEPENDENDO PARA SUA REATIVAÇÃO, DE SOLICITAÇÃO" +
                "PELO TITULAR NOS CANAIS DE ATENDIMENTO E DE UMA NOVA ANÁLISE" +
                "DE CRÉDITO PELO EMISSOR." +
                "\nf. A utilização do CARTÃO após a rescisão deste CONTRATO ficará sujeita" +
                "às sanções penais previstas em lei, sem prejuízo da obrigação de pagar" +
                "os débitos decorrentes dessa utilização, respectivos acréscimos e" +
                "reembolso de custos."));
        itema13.setAlignment(Element.ALIGN_JUSTIFIED);

        list13.add(itema13);
        cell13.addElement(list13);
        table13.addCell(cell13);
        document.add(table13);


        //parte 12


        PdfPTable table14 = new PdfPTable(2);
        table14.setTotalWidth(400);
        table14.setLockedWidth(true);
        table14.setWidths(new int[]{1, 100});
        table14.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cell14;
        cell14 = new PdfPCell();
        cell14.setBorder(PdfPCell.NO_BORDER);
        cell14.addElement(new com.itextpdf.text.Paragraph(""));
        table14.addCell(cell14);
        cell14 = new PdfPCell();
        cell14.setBorder(PdfPCell.NO_BORDER);
        com.itextpdf.text.List list14 = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);


        com.itextpdf.text.Paragraph tittleDoUsoDeDados = new com.itextpdf.text.Paragraph(espaçoTab + "13. DO USO DOS DADOS");
        document.add(tittleDoUsoDeDados);

        ListItem itema14 = new ListItem(new Chunk("\na. Aderindo ao presente CONTRATO o titular do cartão" +
                "declara estar" +
                "ciente e autoriza que seus dados e do PORTADOR de CARTÃO o que inclui" +
                "dados cadastrais, operacionais e financeiros, passem a compor o bancode dados do EMISSOR. O banco de dados formado é de propriedade e" +
                "responsabilidade do EMISSOR, sendo que o seu uso, acesso e" +
                "compartilhamento, quando necessários, serão feitos dentro dos limites" +
                "e propósitos de suas atividades, podendo, neste sentido, ser fornecido" +
                "para as empresas do grupo Nova Era, disponibilizado para consulta e" +
                "cedido a seus parceiros de negócios, fornecedores e autoridades, desde" +
                "que obedecido ao disposto em sua Política de Privacidade/Termo de Uso" +
                "e observando as disposições legais sobre sigilo das informações."));
        itema14.setAlignment(Element.ALIGN_JUSTIFIED);

        list14.add(itema14);
        cell14.addElement(list14);
        table14.addCell(cell14);
        document.add(table14);


        //parte 13


        PdfPTable table15 = new PdfPTable(2);
        table15.setTotalWidth(400);
        table15.setLockedWidth(true);
        table15.setWidths(new int[]{1, 100});
        table15.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cell15;
        cell15 = new PdfPCell();
        cell15.setBorder(PdfPCell.NO_BORDER);
        cell15.addElement(new com.itextpdf.text.Paragraph(""));
        table15.addCell(cell15);
        cell15 = new PdfPCell();
        cell15.setBorder(PdfPCell.NO_BORDER);
        com.itextpdf.text.List list15 = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);


        com.itextpdf.text.Paragraph tittleDaVigencia = new com.itextpdf.text.Paragraph(espaçoTab + "14. DA VIGÊNCIA");
        document.add(tittleDaVigencia);

        ListItem itema15 = new ListItem(new Chunk("\na. Este CONTRATO é celebrado por prazo indeterminado, podendo ser" +
                "rescindido por qualquer das partes de forma unilateral, hipótese em que" +
                "ficará automaticamente cancelado o CARTÃO."));
        itema15.setAlignment(Element.ALIGN_JUSTIFIED);

        list15.add(itema15);
        cell15.addElement(list15);
        table15.addCell(cell15);
        document.add(table15);

        //parte 14


        PdfPTable table16 = new PdfPTable(2);
        table16.setTotalWidth(400);
        table16.setLockedWidth(true);
        table16.setWidths(new int[]{1, 100});
        table16.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell cell16;
        cell16 = new PdfPCell();
        cell16.setBorder(PdfPCell.NO_BORDER);
        cell16.addElement(new com.itextpdf.text.Paragraph(""));
        table16.addCell(cell16);
        cell16 = new PdfPCell();
        cell16.setBorder(PdfPCell.NO_BORDER);
        com.itextpdf.text.List list16 = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);


        com.itextpdf.text.Paragraph tittleDisposicoesFinais = new com.itextpdf.text.Paragraph(espaçoTab + "15. DAS DISPOSIÇÕES FINAIS");
        document.add(tittleDisposicoesFinais);

        ListItem itema16 = new ListItem(new Chunk("\na. Na hipótese de alteração do presente CONTRATO, o titular do cartão" +
                "será devidamente informado, tendo acesso ao texto na íntegra no site:" +
                "useteucard.com.br ou nos canais APP e Web. Caso não concorde com a" +
                "alteração você poderá rescindir o CONTRATO. A realização de" +
                "TRANSAÇÃO através do CARTÃO após recebimento da informação da" +
                "alteração do CONTRATO acima mencionada, implicará em concordância" +
                "automática com os novos termos do contrato." +
                "\nb. A tolerância de uma parte para com a outra, relativamente ao" +
                "descumprimento de quaisquer das obrigações ora assumidas, bem como" +
                "o atraso ou omissão por qualquer das partes em exercer qualquer direito" +
                "decorrente do presente CONTRATO NÃO poderão ser considerados" +
                "moratória, novação ou renúncia a qualquer direito, constituindo mera" +
                "liberalidade, que não impedirá a parte tolerante ou omissa de exigir da" +
                "outra o fiel cumprimento deste CONTRATO." +
                "\nc. Este CONTRATO obriga, por todos os seus termos e condições, as partes," +
                "seus herdeiros e sucessores, a qualquer título que o sejam." +
                "\nd. O EMISSOR disponibiliza canal de Serviço de Atendimento ao Cliente" +
                "para informações e reclamações todos os dias, através do número 4003-" +
                "3203." +
                "\ne. Fica eleito o foro da Comarca do domicílio do TITULAR para dirimir" +
                "quaisquer dúvidas ou controvérsias que venham a surgir em virtude" +
                "deste CONTRATO. Este CONTRATO passará a vigorar a partir da data do" +
                "seu registro no competente Cartório de Registro de Títulos e Documentos."));
        itema16.setAlignment(Element.ALIGN_JUSTIFIED);

        com.itextpdf.text.Paragraph textConsideracoesFianis = new com.itextpdf.text.Paragraph("\n\n\n\nAs cláusulas desse CONTRATO estão regularmente depositadas no cartório de Registro e Títulos e Documentos XXXXXXXXXXXXXX, sob o no XXXXX, em livro XXXX em XX de XX de XX.");
        textConsideracoesFianis.setAlignment(Element.ALIGN_JUSTIFIED);

        LocalDate data = LocalDate.now();
        Locale local = new Locale("pt", "BR");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMMM", local);

        com.itextpdf.text.Paragraph textTitularCartao = new com.itextpdf.text.Paragraph("\n" + proposta.getCliente().getNome() +
                "\nPortador Titular - XXXXX" +
                "\n\nTeuCard" +
                "\n\nManaus, " + data.getDayOfMonth() + " de " + fmt.format(data) + " de " + data.getYear() + ".");
        textTitularCartao.setAlignment(Element.ALIGN_LEFT);


        list16.add(itema16);
        cell16.addElement(list16);
        table16.addCell(cell16);
        document.add(table16);
        document.add(textConsideracoesFianis);
        document.add(textTitularCartao);

        //Colocando imagem
        byte[] imageByte = Base64.decode(propostaCartaoRequest.getAssinaturaImage());
        com.itextpdf.text.Image image = Image.getInstance(imageByte);
        image.scaleAbsolute(269, 139);
        document.add(image);
        document.add(new Paragraph("Data assinatura: " + propostaCartaoRequest.getData()));
        document.close();

        byte[] pdfByte = FileUtils.readFileToByteArray(file);


        file.delete();

        return pdfByte;
    }

    public void salvarAssinaturaPdf(PropostaCartaoRequest propostaCartaoRequest, byte[] file) {
        Proposta proposta = Proposta.findById(new ObjectId(propostaCartaoRequest.getId()));
        proposta.setAssinaturaPdf(file);
        proposta.setSituacao(Situacao.ASSINADA);
        proposta.update();
    }

    public byte[] getPropostaPdf(String id) {
        Proposta proposta = Proposta.findById(new ObjectId(id));
        return proposta.getAssinaturaPdf();
    }

    public PropostaAssinaturaResponse getPropostaAssinatura(String id) {
        Proposta proposta = Proposta.findById(new ObjectId(id));
        PropostaAssinaturaResponse propostaAssinaturaResponse = propostaMapper.toPropostaAssinaturaDto(proposta);
        return propostaAssinaturaResponse;
    }

    public void recusarProposta(String propostaId, MotivoStatusRequest recusarPropostaRequest) throws PropostaInvalidaException {
        Proposta proposta = Proposta.findById(new ObjectId(propostaId));
        if (proposta == null) {
            throw new PropostaInvalidaException("Proposta não encontrada.");
        }
        proposta.setStatus(Status.REPROVADA);
        proposta.setDataReprovacao(LocalDateTime.now());
        proposta.update();
        utils.persistirMotivoStatus(Status.REPROVADA, propostaId, recusarPropostaRequest.getMotivoStatus(), proposta.getCpf());
    }

    public void manterPropostaAnalise(String propostaId, ManterAnalisePropostaRequest request) throws PropostaInvalidaException {
        Proposta proposta = Proposta.findById(new ObjectId(propostaId));
        if (proposta == null) {
            throw new PropostaInvalidaException("Proposta não encontrada.");
        }
        proposta.setStatus(Status.PENDENTE);
        proposta.setDataAnalise(LocalDateTime.now());
        proposta.update();
        utils.persistirMotivoStatus(Status.PENDENTE, propostaId, request.getMotivo(), proposta.getCpf());
    }

    public BuscaPropostaResponseList retornarPropostaNomeCpf(String nome, String cpf, String de, String ate) throws PropostaInvalidaException {
        BuscaPropostaResponseList responseList = new BuscaPropostaResponseList();
        List<Proposta> lista = new ArrayList<>();
        LocalDateTime dataInicial = LocalDateTime.now().minus(24, ChronoUnit.HOURS);
        if (cpf == null && nome == null) {
            lista = Proposta.listarProposta(cpf, nome, de, ate).stream().filter(teste -> teste.getDataInclusao().isAfter(dataInicial)).collect(Collectors.toList());
        } else {
            lista = Proposta.listarProposta(cpf, nome, de, ate);
        }
        lista.stream().forEach(proposta -> {
            String strDataAnalise = "";
            BuscaPropostaResponse response = new BuscaPropostaResponse();
            response.setId(proposta.getId().toString());
            response.setNomeCliente(proposta.getCliente() != null ? proposta.getCliente().getNome() : "");
            response.setCpf(proposta.getCpf());
            response.setStatus(proposta.getStatus() + " - " + proposta.getSituacao());
            response.setAtendente(proposta.getAtendente().getNome());
            LocalDateTime iFinal = LocalDateTime.now();
            LocalDateTime iInicial = proposta.getDataInclusao();
            Duration d = Duration.between(iInicial, iFinal);
            long days = d.toDays();
            d = d.minusDays(days);
            long hours = d.toHours();
            d = d.minusHours(hours);
            long minutes = d.toMinutes();
            d = d.minusMinutes(minutes);
            long seconds = d.getSeconds();
            String duracao = (days == 0 ? "00:" : days < 10 ? "0" + days + ":" : days + ":") +
                    (hours == 0 ? "00:" : hours < 10 ? "0" + hours + ":" : hours + ":") +
                    (minutes == 0 ? "00:" : minutes < 10 ? "0" + minutes + ":" : minutes + ":") +
                    (seconds == 0 ? seconds < 10 ? "0" + seconds : "00" : seconds);
            response.setDuracao(duracao);
            switch (proposta.getStatus()) {
                case REPROVADA:
                    strDataAnalise = proposta.getDataReprovacao().toString();
                    break;
                case APROVADA:
                    strDataAnalise = proposta.getDataAprovacao().toString();
                    break;
                case CANCELADA:
                    strDataAnalise = proposta.getDataCancelamento().toString();
                    break;
                case ANALISE:
                    strDataAnalise = proposta.getDataAnalise().toString();
                    break;
                case PENDENTE:
                    strDataAnalise = proposta.getDataAnalise().toString();
                    break;
                default:
                    strDataAnalise = "";
            }
            response.setDataAnalise(strDataAnalise);

            responseList.addBuscaPropostaResponse(response);
        });
        return responseList;
    }

    public void salvarContato(String propostaId, ContatoRequest contatoRequest) throws ContatoInvalidoException {
        List<Contato> contatoLista = Contato.findByIdProposta(propostaId);
        if (contatoLista.size() >= 2) {
            throw new ContatoInvalidoException("Limite máximo de contato 2");
        } else {
            Contato contato = contatoMapper.fromResource(contatoRequest);
            contato.setIdProposta(propostaId);
            contato.setData(LocalDateTime.now());
            contato.persist();
        }
    }

    public List<ContatoResponse> listarContatosProposta(String propostaId) throws ContatoInvalidoException {
        List<Contato> contatoLista = Contato.findByIdProposta(propostaId);
        if (contatoLista == null) {
            throw new ContatoInvalidoException("Nenhum contato encontrado para a proposta");
        } else {
            List<ContatoResponse> contatoResponses = contatoMapper.toResource(contatoLista);
            return contatoResponses;
        }
    }

    public DadosDto obterDadosPropostaSpc(String idProposta) {
        Proposta proposta = Proposta.findById(new ObjectId(idProposta));
        Spc spc = Spc.buscarPorPropostaId(idProposta);
        List<Contato> contatos = Contato.findByIdProposta(idProposta);
        List<MotivoStatus> motivo = MotivoStatus.buscarMotivoPorIdProposta(idProposta);
        return utils.mapperDadosDto(proposta, contatos, spc, motivo, idProposta);
    }

    public void alterarDadosProposta(PropostaAtualizadaRequest request) throws PropostaInvalidaException {
        Proposta proposta = Proposta.findById(new ObjectId(request.getPropostaId()));
        try {

            if (null != request.getNome() && !"".equals(request.getNome())) {
                proposta.getCliente().setNome(request.getNome());
            }

            if (null != request.getTelefone() && !"".equals(request.getTelefone())) {
                proposta.getCliente().setCelular(request.getTelefone());
            }

            if (null != request.getDataNascimento() && !"".equals(request.getDataNascimento())) {
                proposta.getCliente().setDataNascimento(LocalDate.parse(request.getDataNascimento()));
            }

            /* Endereço */
            if (null != request.getEndereco()) {
                EnderecoDTO endereco = request.getEndereco();
                if (null != endereco.getCep() && !"".equals(endereco.getCep())) {
                    proposta.getCliente().getEndereco().setCep(endereco.getCep());
                }

                if (null != endereco.getLogradouro() && !"".equals(endereco.getLogradouro())) {
                    proposta.getCliente().getEndereco().setLogradouro(endereco.getLogradouro());
                }

                if (null != endereco.getNumero() && !"".equals(endereco.getNumero())) {
                    proposta.getCliente().getEndereco().setNumero(endereco.getNumero());
                }

                if (null != endereco.getComplemento() && !"".equals(endereco.getComplemento())) {
                    proposta.getCliente().getEndereco().setComplemento(endereco.getComplemento());
                }

                if (null != endereco.getCidade() && !"".equals(endereco.getCidade())) {
                    proposta.getCliente().getEndereco().setCidade(endereco.getCidade());
                }

                if (null != endereco.getBairro() && !"".equals(endereco.getBairro())) {
                    proposta.getCliente().getEndereco().setBairro(endereco.getBairro());
                }

            }

            /* RENDA INFORMADA */
            if (null != request.getRendaInformada()) {
                proposta.setRendaInformada(request.getRendaInformada());
            }

            /*  INFORMACAO PROFISSIONAL */
            if (null != request.getProfissao()) {
                proposta.setProfissao(request.getProfissao());
            }

            /*  INFORMACAO DE EMPREGO FORMAL */
            if (null != request.getEmpregoFormal() && !"".equals(request.getEmpregoFormal())) {
                proposta.setEmpregoFormal(request.getEmpregoFormal());
            }


        } catch (Exception e) {
            logger.error(String.format("Erro ao alterar os campos da proposta %s, favor contactar suporte.", request.getPropostaId()));
            throw new PropostaInvalidaException("Erro ao atualizar os dados.");
        }

        logger.info(String.format("Atualizando Proposta %s", request.getPropostaId()));
        proposta.update();
    }

    private void atualizarStatusProposta(Proposta proposta, Status status) {
        proposta.setStatus(status);
        proposta.update();
    }

    private ClientePropostaRequest gerarLimiteCredito(ClientePropostaRequest clientePropostaRequest, Integer score, Boolean trabalhoFormal, BigDecimal renda) {
        MatrizDefinicaoLimiteRequest matrizDefinicaoLimiteRequest = new MatrizDefinicaoLimiteRequest();
        matrizDefinicaoLimiteRequest.setCep(clientePropostaRequest.getEndereco().getCep());
        matrizDefinicaoLimiteRequest.setDataNascimento(clientePropostaRequest.getDataNascimento().toString());
        matrizDefinicaoLimiteRequest.setEmpregoFormal(trabalhoFormal);
        matrizDefinicaoLimiteRequest.setRenda(renda.intValue());
        matrizDefinicaoLimiteRequest.setScore(score);

        System.out.println(matrizDefinicaoLimiteRequest.getCep() + " cep");
        System.out.println(matrizDefinicaoLimiteRequest.getDataNascimento() + " nascimento");
        System.out.println(matrizDefinicaoLimiteRequest.getEmpregoFormal() + " empregoFormal");
        System.out.println(matrizDefinicaoLimiteRequest.getRenda() + " renda");
        System.out.println(matrizDefinicaoLimiteRequest.getScore() + " score");

        //valida proposta aprovada identificar valor do credito
        MatrizDefinicaoLimiteResponse credito = motorClient.validarPropostaMatrizDefinicaoLimite(matrizDefinicaoLimiteRequest);

        //atribui valor de limite de crédito
        clientePropostaRequest.setLimiteCredito(new BigDecimal(credito.getLimite()));

        return clientePropostaRequest;

    }

    public List<PropostaDto> obterRelatorioProposta(RelatorioPropostaRequest request) {
        List<Proposta> propostas = Proposta.relatorioProposta(request);
        List<PropostaDto> dtos = new ArrayList<PropostaDto>();

        propostas.stream().forEach(proposta -> {
            PropostaDto dto = new PropostaDto();
            dto.setId(proposta.getId().toString());
            if (proposta.getCliente() != null) {
                dto.setNome(proposta.getCliente().getNome());
                dto.setCpf(proposta.getCliente().getCpf());
            }
            dto.setStatus(proposta.getStatus());
            dto.setDataInclusao(proposta.getDataInclusao());
            dtos.add(dto);
        });
        return dtos;
    }


    public Optional<Proposta> obterPropostaPorId(String propostaId) {
        return Optional.of(Proposta.findById(new ObjectId(propostaId)));
    }


    public Optional<List<Proposta>> obterStatusProposta(StatusPropostaRequest request) {
        return Optional.of(Proposta.obterStatusProposta(request));
    }

    public void salvarDocumentosIdentificacaoCliente(IdentificacaoClienteRequest identificacaoClienteRequest) throws IOException {
        String DEST_DOCUMENTO_FRENTE = System.getProperty("user.dir") + "/documentos/propostaDocumento/DOCUMENTO_FRENTE_" + identificacaoClienteRequest.getIdProposta() + ".jpg";
        String DEST_DOCUMENTO_VERSO = System.getProperty("user.dir") + "/documentos/propostaDocumento/DOCUMENTO_VERSO_" + identificacaoClienteRequest.getIdProposta() + ".jpg";
        String DEST_FOTO = System.getProperty("user.dir") + "/documentos/fotoCertiface/FOTO_PROPOSTA_" + identificacaoClienteRequest.getIdProposta() + ".jpg";

        if (identificacaoClienteRequest.getDocumentoFrente() != null && identificacaoClienteRequest.getDocumentoVerso() != null) {
            File fileDocumentoFrente = new File(DEST_DOCUMENTO_FRENTE);
            File fileDocumentoVerso = new File(DEST_DOCUMENTO_VERSO);

            //Frente
            fileDocumentoFrente.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(fileDocumentoFrente);
            byte[] base64Documento = Base64.decode(identificacaoClienteRequest.getDocumentoFrente());
            fos.write(base64Documento);

            //Verso
            fileDocumentoVerso.getParentFile().mkdirs();
            FileOutputStream fosVerso = new FileOutputStream(fileDocumentoVerso);
            byte[] base64DocumentoVerso = Base64.decode(identificacaoClienteRequest.getDocumentoVerso());
            fosVerso.write(base64DocumentoVerso);
        }

        if (identificacaoClienteRequest.getFotoCertiface() != null) {
            File fileFoto = new File(DEST_FOTO);
            fileFoto.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(fileFoto);
            byte[] base64Documento = Base64.decode(identificacaoClienteRequest.getFotoCertiface());
            fos.write(base64Documento);
        }
    }

    public IdentificacaoClienteResponse encontrarDocumentosIdentificacaoCliente(String idProposta) throws IOException {
        File fileDocumentoFrente = new File(System.getProperty("user.dir") + "/documentos/propostaDocumento/DOCUMENTO_FRENTE_" + idProposta + ".jpg");
        File fileDocumentoVerso = new File(System.getProperty("user.dir") + "/documentos/propostaDocumento/DOCUMENTO_VERSO_" + idProposta + ".jpg");
        File fileFoto = new File(System.getProperty("user.dir") + "/documentos/fotoCertiface/FOTO_PROPOSTA_" + idProposta + ".jpg");
        IdentificacaoClienteResponse identificacaoClienteResponse = new IdentificacaoClienteResponse();
        identificacaoClienteResponse.setIdProposta(idProposta);

        //Frente
        if (fileDocumentoFrente.exists()) {
            identificacaoClienteResponse.setDocumentoFrente(Base64.encode(FileUtils.readFileToByteArray(fileDocumentoFrente)));
        }
        //Verso
        if (fileDocumentoVerso.exists()) {
            identificacaoClienteResponse.setDocumentoVerso(Base64.encode(FileUtils.readFileToByteArray(fileDocumentoVerso)));
        }
        //Foto
        if (fileFoto.exists()) {
            identificacaoClienteResponse.setFotoCertiface(Base64.encode(FileUtils.readFileToByteArray(fileFoto)));
        }

        return identificacaoClienteResponse;
    }

    public void desatribuirProposta(String idProposta) throws PropostaInvalidaException {
        Proposta proposta = Proposta.findById(new ObjectId(idProposta));
        if (proposta == null) {
            throw new PropostaInvalidaException("Não existe proposta com esse id.");
        }
        proposta.setStatus(Status.PENDENTE);
        proposta.update();
    }

    public void atribuirProposta(String idProposta, AtribuirPropostaRequest atribuirPropostaRequest) throws PropostaInvalidaException {
        Proposta proposta = Proposta.findById(new ObjectId(idProposta));
        if (proposta == null) {
            throw new PropostaInvalidaException("Não existe proposta com esse id.");
        }

        //insere na proposta funcionario que esta atualizando a proposta
        FuncionarioResponse funcionarioResponse = funcionarioClient.buscarFuncionarioPorIdUsuario(atribuirPropostaRequest.getIdUsuario());
        Funcionario operador = new Funcionario();

        //gravar o id do Usuario
        operador.setId(atribuirPropostaRequest.getIdUsuario());
        operador.setNome(funcionarioResponse.getNome());
        proposta.setOperador(operador);
        proposta.setStatus(Status.ANALISE);
        proposta.setSituacao(Situacao.MESA_CREDITO);
        proposta.update();
    }

    public String gerarPdf(String tokenCartao, String mes, String ano) throws IOException, DocumentException {

        String DEST = "/home/opah/arquivos/faturas/fatura.pdf";

        File file = new File(DEST);
        file.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(DEST);
        com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(fos);
        PdfDocument pdf = new PdfDocument(writer);


        com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdf);
        document.setMargins(18f, 80f, 18f, 80f);


        //recuperando tokenCartao/numeroCartao
        RetornaTokenCartaoRequest retornaTokenCartaoRequest = new RetornaTokenCartaoRequest();
        retornaTokenCartaoRequest.setNumeroCartao(tokenCartao);

        FaturaResponse fatura = new FaturaResponse();
        ClienteResponse cliente = new ClienteResponse();
        ResponseTokenCartao responseTokenCartao = autorizadorClient.obterTokenCartao(retornaTokenCartaoRequest);

        fatura = clienteClient.obterFatura(responseTokenCartao.getTokenCartao().getNumeroCartao(), mes, ano);
        //recuperando Cliente pelo numero cartão

        cliente = clienteClient.buscarClientePorNumeroCartao(fatura.getNumeroCartao());
        fatura.setCliente(cliente);


        byte[] imageByte = Base64.decode("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");
        ImageData imageData = ImageDataFactory.create(imageByte);
        com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(imageData);
        image.scaleAbsolute(150, 50);
        image.setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.add(image);

        com.itextpdf.layout.element.Paragraph slogan = new com.itextpdf.layout.element.Paragraph("O Cartão que é a tua cara!\n\n");
        slogan.setTextAlignment(TextAlignment.CENTER);
        slogan.setFontSize(10f);
        document.add(slogan);


        String dadosClienteParagrafo = cliente.getNome() + "\n" +
                cliente.getEndereco().getLogradouro() + " , " + cliente.getEndereco().getNumero()
                + " " + cliente.getEndereco().getBairro() + " " +
                cliente.getEndereco().getCep() + " - " + cliente.getEndereco().getCidade();


        com.itextpdf.layout.element.Paragraph dadosCliente = new com.itextpdf.layout.element.Paragraph(dadosClienteParagrafo);
        dadosCliente.setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(dadosCliente);

        com.itextpdf.layout.element.Paragraph nomeTitularCartao = new com.itextpdf.layout.element.Paragraph("\nTitular: " + cliente.getNome() + "\n" +
                "Cartão: " + fatura.getNumeroCartao());
        nomeTitularCartao.setTextAlignment(TextAlignment.LEFT);
        document.add(nomeTitularCartao);

        String dataVencimento = fatura.getDataVencimento().getDayOfMonth() + "/" +
                fatura.getDataVencimento().getMonthValue() + "/" + fatura.getDataVencimento().getYear();

        com.itextpdf.layout.element.Paragraph vencimentoFatura = new com.itextpdf.layout.element.Paragraph(
                "\nVencimento : " + dataVencimento + "\n" + "Fechamento próxima fatura: ");
        vencimentoFatura.setTextAlignment(TextAlignment.LEFT);
        document.add(vencimentoFatura);

        com.itextpdf.layout.element.Paragraph resumoFaturaEmReais = new com.itextpdf.layout.element.Paragraph("\nResumo da fatura em R$");
        resumoFaturaEmReais.setBold();
        document.add(resumoFaturaEmReais);

        com.itextpdf.layout.element.Paragraph resumoFatura = new com.itextpdf.layout.element.Paragraph(
                "Total da fatura anterior : R$: " + fatura.getSaldoFaturaAnterior() + "\n" + "Pagamento efetuado em " +
                        String.valueOf(fatura.getDataPagamento() != null ? fatura.getDataPagamento() : "") + ": R$ " + fatura.getPagamentoFaturaAnterior() + "\n");
        com.itextpdf.layout.element.Paragraph total = new com.itextpdf.layout.element.Paragraph("Total desta fatura R$: " + fatura.getSaldoFaturaAtual());
        resumoFatura.setMarginTop(-5f);
        total.setTextAlignment(TextAlignment.LEFT);
        total.setMarginTop(-5f);
        total.setBold();
        document.add(resumoFatura);
        document.add(total);


        //todo aqui vão os retangulos

        Table table5 = new Table(3);
        table5.addCell(getCell("\n\nVencimento\n",
                TextAlignment.LEFT));
        table5.addCell(getCell("\n\nPagamento Total\n"
                , TextAlignment.CENTER));
        table5.addCell(getCell("\n\nPagamento Minimo\n"
                , TextAlignment.CENTER));


        Table table6 = new Table(3);
        table5.addCell(getCell("" + dataVencimento,
                TextAlignment.LEFT).setBold());
        table5.addCell(getCell("R$ " + String.valueOf(fatura.getValorPagamento() != null && fatura.getValorPagamento() != new BigDecimal(0) ? fatura.getValorPagamento() : new BigDecimal(0.0))
                , TextAlignment.CENTER).setBold());
        table5.addCell(getCell("R$ " + fatura.getValorMinimoRecebido()
                , TextAlignment.CENTER).setBold());


        //todo fim dos retangulos
        com.itextpdf.layout.element.Paragraph taxas = new com.itextpdf.layout.element.Paragraph("\n\nTaxas: ");
        taxas.setBold();
        com.itextpdf.layout.element.Paragraph atual = new com.itextpdf.layout.element.Paragraph("Atual: ");
        atual.setTextAlignment(TextAlignment.CENTER);
        atual.setBold();
        com.itextpdf.layout.element.Paragraph proximoperiodo = new com.itextpdf.layout.element.Paragraph("Próx Período: ");
        proximoperiodo.setTextAlignment(TextAlignment.RIGHT);
        proximoperiodo.setBold();
        proximoperiodo.setMarginTop(-20f);


        Table table3 = new Table(3);
        table3.addCell(getCell("\nRotativo : " + "\n" + "Compra Par com Juros: " + "\n"
                + "IOF: ", TextAlignment.LEFT));
        table3.addCell(getCell("\n" + String.valueOf(fatura.getTaxaRotativoAtual() != null && fatura.getTaxaRotativoAtual() != new BigDecimal(0)
                ? fatura.getTaxaRotativoAtual() : new BigDecimal(0.0)) + "%" + "\n"
                + fatura.getTaxaJurosAtual() + "%" + "\n 0,38%", TextAlignment.CENTER));
        table3.addCell(getCell("\n" + fatura.getTaxaRotativoProximo() + "%" + "\n"
                        + fatura.getTaxaJurosProximo() + "%"
                , TextAlignment.RIGHT));



        com.itextpdf.layout.element.Paragraph lancamentos = new com.itextpdf.layout.element.Paragraph("\n\n\n\n\n\n\n\n\n\n\nLançamentos - Compras: \n" + cliente.getNome() +
                "   (Final   " + fatura.getNumeroCartao().substring(12, fatura.getNumeroCartao().length()) + ")").setBold();


        Table table4 = new Table(3);
        table4.addCell(getCell("\nData: ", TextAlignment.LEFT).setBold());
        table4.addCell(getCell("\nEstabelecimento: ", TextAlignment.CENTER).setBold());
        table4.addCell(getCell("\nValor em R$: ", TextAlignment.RIGHT).setBold());

        for (LancamentoResponse lancamento1 : fatura.getLancamentos()) {
            table4.addCell(getCell("\n" + lancamento1.getData(), TextAlignment.LEFT));
            table4.addCell(getCell("\n" + lancamento1.getEstabelecimento(), TextAlignment.CENTER));
            table4.addCell(getCell("\nR$: " + lancamento1.getValor(), TextAlignment.RIGHT));
        }

        com.itextpdf.layout.element.Paragraph linhaDigitavel = new com.itextpdf.layout.element.Paragraph("\nLinha Digitável: " + fatura.getLinhaDigitavel());
        linhaDigitavel.setTextAlignment(TextAlignment.CENTER);


        com.itextpdf.layout.element.Paragraph totalFatura = new com.itextpdf.layout.element.Paragraph("\n\nTotal da Fatura:  R$");
        totalFatura.setTextAlignment(TextAlignment.LEFT);
        totalFatura.setBold();

        com.itextpdf.layout.element.Paragraph totalValor = new com.itextpdf.layout.element.Paragraph("" + fatura.getSaldoFaturaAtual() + "\n\n\n");
        totalValor.setTextAlignment(TextAlignment.RIGHT);
        totalValor.setMarginTop(-20f);
        totalValor.setBold();


        document.add(table5);
        document.add(table6);


        document.add(taxas);
        document.add(atual);
        document.add(proximoperiodo);
        document.add(table3);
        document.add(lancamentos);
        document.add(table4);
        document.add(totalFatura);
        document.add(totalValor);

        if (fatura.getLinhaDigitavel() != null) {
            document.add(linhaDigitavel);
        }

        //codigo de barras
        if (fatura.getCodigoDeBarras() != null) {
            CreateBarcodePdf geraPdf = new CreateBarcodePdf();
            String codebar = Base64.encode(geraPdf.geraBarCode(fatura.getCodigoDeBarras()));
            byte[] codebarImage = Base64.decode(codebar);
            ImageData imageDataCodeBar = ImageDataFactory.create(codebarImage);
            com.itextpdf.layout.element.Image imageCodeBar = new com.itextpdf.layout.element.Image(imageDataCodeBar);
            imageCodeBar.scaleAbsolute(imageCodeBar.getImageWidth(), imageCodeBar.getImageHeight());
            imageCodeBar.setHorizontalAlignment(HorizontalAlignment.CENTER);
            document.add(imageCodeBar);
        }


        document.close();

        byte[] pdfByte = FileUtils.readFileToByteArray(file);

        String base64Document = Base64.encode(pdfByte);


        //file.delete();

        return base64Document;
    }


    public Cell getCell(String text, TextAlignment alignment) {
        Cell cell = new Cell().add(new com.itextpdf.layout.element.Paragraph(text));
        cell.setPadding(0);
        cell.setTextAlignment(alignment);
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }


    public FaturaResponse obterDadosFatura(String tokenCartao, String mes, String ano) {

        RetornaTokenCartaoRequest retornaTokenCartaoRequest = new RetornaTokenCartaoRequest();
        retornaTokenCartaoRequest.setNumeroCartao(tokenCartao);

        FaturaResponse fatura = new FaturaResponse();
        ClienteResponse cliente = new ClienteResponse();
        ResponseTokenCartao responseTokenCartao = autorizadorClient.obterTokenCartao(retornaTokenCartaoRequest);


        fatura = clienteClient.obterFatura(responseTokenCartao.getTokenCartao().getNumeroCartao(), mes, ano);
        //recuperando Cliente pelo numero cartão
        cliente = clienteClient.buscarClientePorNumeroCartao(fatura.getNumeroCartao());
        fatura.setCliente(cliente);


        return fatura;
    }


    public FaturaResponseList obterListaFaturas(String numeroCartao, String cpf) {

        FaturaResponseList faturaResponseList = clienteClient.obterListaFaturas(numeroCartao, cpf);
        System.out.println(faturaResponseList);

        return faturaResponseList;

    }


}
