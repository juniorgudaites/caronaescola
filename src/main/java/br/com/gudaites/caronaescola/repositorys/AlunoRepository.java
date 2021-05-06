package br.com.gudaites.caronaescola.repositorys;

import br.com.gudaites.caronaescola.codecs.AlunoCodec;
import br.com.gudaites.caronaescola.models.Aluno;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Repository da Collection Aluno
 *
 * @author Jair Gudaites Junior
 */

@Repository
public class AlunoRepository {

    private MongoClient cliente;
    private MongoDatabase bancoDeDados;

    //    Criar Conexão com MongoDB
    private void criarConexao() {
        Codec<Document> codec = MongoClient.getDefaultCodecRegistry().get(Document.class);
        AlunoCodec alunoCodec = new AlunoCodec(codec);

        CodecRegistry registro = CodecRegistries.fromRegistries(
                MongoClient.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(alunoCodec));

        MongoClientOptions options = MongoClientOptions.builder().codecRegistry(registro).build();

        cliente = new MongoClient("localhost:27017", options);
        bancoDeDados = cliente.getDatabase("caronaescola");
    }

    //    Fechar Conexão com MongoDB
    private void fecharConexao() {
        this.cliente.close();
    }

    //    Salvar aluno no MongoDB
    public void salvar(Aluno aluno) {
        criarConexao();
        MongoCollection<Aluno> alunos = bancoDeDados.getCollection("aluno", Aluno.class);

        if (aluno.getId() == null) {
            alunos.insertOne(aluno);
        } else {
            alunos.updateOne(Filters.eq("_id", aluno.getId()), new Document("$set", aluno));
        }

        fecharConexao();
    }

    //    GET todos alunos cadastrados
    public List<Aluno> obterTodosAlunos() {
        criarConexao();
        MongoCollection<Aluno> alunos = bancoDeDados.getCollection("aluno", Aluno.class);
        MongoCursor<Aluno> resultados = alunos.find().iterator();
        List<Aluno> alunosEncontrados = popularAlunos(resultados);
        fecharConexao();

        return alunosEncontrados;
    }

    //    Pesquisa por ID
    public Aluno obterAlunoPor(String id) {
        criarConexao();
        MongoCollection<Aluno> alunos = this.bancoDeDados.getCollection("aluno", Aluno.class);
        Aluno aluno = alunos.find(Filters.eq("_id", new ObjectId(id))).first();
        fecharConexao();
        return aluno;
    }

    //    Pesquisa por nome
    public List<Aluno> pesquisarPor(String nome) {
        criarConexao();
        MongoCollection<Aluno> alunoCollection = this.bancoDeDados.getCollection("aluno", Aluno.class);
        MongoCursor<Aluno> resultados = alunoCollection.find(Filters.eq("nome", nome), Aluno.class).iterator();
        List<Aluno> alunos = popularAlunos(resultados);
        fecharConexao();
        return alunos;
    }


    //    Pupulando alunos do retorno MongoCursor
    private List<Aluno> popularAlunos(MongoCursor<Aluno> resultados) {
        List<Aluno> alunos = new ArrayList<>();
        while (resultados.hasNext()) {
            alunos.add(resultados.next());
        }
        return alunos;
    }

    //    Pesquisa por nota aprovado/reprovado
    public List<Aluno> pesquisarPor(String classificacao, double nota) {
        criarConexao();

        MongoCollection<Aluno> alunoCollection = this.bancoDeDados.getCollection("aluno", Aluno.class); // GET Collection
        MongoCursor<Aluno> resultados = null;
        if (classificacao.equals("reprovados")) {
            resultados = alunoCollection.find(Filters.lt("notas", nota)).iterator(); // FIND alunos reprovados
        } else if (classificacao.equals("aprovados")) {
            resultados = alunoCollection.find(Filters.gte("notas", nota)).iterator(); // FIND alunos aprovados
        }

        List<Aluno> alunos = popularAlunos(resultados);

        fecharConexao();

        return alunos;
    }

    //    Pesquisa por Geolocalização (Latidude e Longetude) API Google Maps Geocoding
    public List<Aluno> pesquisaPorGeolocalizacao(Aluno aluno) {

        criarConexao();

        MongoCollection<Aluno> alunoCollection = this.bancoDeDados.getCollection("aluno", Aluno.class); // GET Collection
        alunoCollection.createIndex(Indexes.geo2dsphere("contato")); // Criar Index geo2d
        List<Double> coordinates = aluno.getContato().getCoordinates(); // GET coordenadas dos aluno
        Point pontoReferencia = new Point(new Position(coordinates.get(0), coordinates.get(1))); // Criando ponto de referência

        MongoCursor<Aluno> resultados = alunoCollection.find(Filters.nearSphere("contato", pontoReferencia, 2000.0, 0.0))
                .limit(5).iterator();
        //      limit = Limite de alunos para mostrar no mapa
        //      skip = Para pular o próprio aluno, pois aluno mais proxímo será o mesmo
        List<Aluno> alunos = popularAlunos(resultados);

        fecharConexao();

        return alunos;
    }
}