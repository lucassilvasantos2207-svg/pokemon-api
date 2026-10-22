const API = "/pokemons";
const POR_PAGINA = 12;

const TIPOS = [
  "normal", "fire", "water", "grass", "electric", "ice", "fighting",
  "poison", "ground", "flying", "psychic", "bug", "rock", "ghost",
  "dragon", "dark", "steel", "fairy"
];

let pagina = 0;
let totalPaginas = 0;

const lista = document.getElementById("lista");
const mensagem = document.getElementById("mensagem");
const campoBusca = document.getElementById("busca");
const selectTipo = document.getElementById("tipo");
const paginacao = document.getElementById("paginacao");
const btnAnterior = document.getElementById("anterior");
const btnProxima = document.getElementById("proxima");
const spanPagina = document.getElementById("pagina");

// Preenche o select de tipos
TIPOS.forEach(t => {
  const opcao = document.createElement("option");
  opcao.value = t;
  opcao.textContent = t.charAt(0).toUpperCase() + t.slice(1);
  selectTipo.appendChild(opcao);
});

function criarCard(p) {
  const tipos = p.tipos
    .map(t => `<span class="tipo tipo-${t}">${t}</span>`)
    .join("");

  return `
    <article class="card">
      <img src="${p.sprite}" alt="${p.nome}">
      <div class="numero">#${String(p.id).padStart(3, "0")}</div>
      <h2>${p.nome}</h2>
      <div class="tipos">${tipos}</div>
      <div class="stats">
        <span>HP: ${p.hp}</span>
        <span>ATK: ${p.ataque}</span>
        <span>DEF: ${p.defesa}</span>
        <span>SPD: ${p.velocidade}</span>
      </div>
    </article>
  `;
}

function mostrar(pokemons) {
  lista.innerHTML = pokemons.map(criarCard).join("");
}

async function carregarLista() {
  mensagem.textContent = "";
  paginacao.style.display = "flex";

  let url = `${API}?page=${pagina}&size=${POR_PAGINA}&sort=id`;
  if (selectTipo.value) {
    url += `&tipo=${selectTipo.value}`;
  }

  try {
    const resposta = await fetch(url);
    if (!resposta.ok) throw new Error("Erro ao buscar a lista");

    const dados = await resposta.json();
    totalPaginas = dados.totalPages;

    mostrar(dados.content);
    spanPagina.textContent = `Página ${pagina + 1} de ${Math.max(totalPaginas, 1)}`;
    btnAnterior.disabled = pagina === 0;
    btnProxima.disabled = pagina + 1 >= totalPaginas;
  } catch (erro) {
    lista.innerHTML = "";
    mensagem.textContent = "Não consegui carregar os Pokémon. A API está rodando?";
  }
}

async function buscarPorNome() {
  const nome = campoBusca.value.trim().toLowerCase();
  if (!nome) {
    carregarLista();
    return;
  }

  mensagem.textContent = "";
  paginacao.style.display = "none";

  try {
    const resposta = await fetch(`${API}/nome/${encodeURIComponent(nome)}`);
    if (resposta.status === 404) {
      lista.innerHTML = "";
      mensagem.textContent = `Nenhum Pokémon encontrado com o nome "${nome}".`;
      return;
    }
    if (!resposta.ok) throw new Error("Erro na busca");

    const pokemon = await resposta.json();
    mostrar([pokemon]);
  } catch (erro) {
    lista.innerHTML = "";
    mensagem.textContent = "Erro ao buscar. A API está rodando?";
  }
}

document.getElementById("btnBuscar").addEventListener("click", buscarPorNome);

campoBusca.addEventListener("keydown", e => {
  if (e.key === "Enter") buscarPorNome();
});

selectTipo.addEventListener("change", () => {
  campoBusca.value = "";
  pagina = 0;
  carregarLista();
});

document.getElementById("btnLimpar").addEventListener("click", () => {
  campoBusca.value = "";
  selectTipo.value = "";
  pagina = 0;
  carregarLista();
});

btnAnterior.addEventListener("click", () => {
  if (pagina > 0) {
    pagina--;
    carregarLista();
  }
});

btnProxima.addEventListener("click", () => {
  if (pagina + 1 < totalPaginas) {
    pagina++;
    carregarLista();
  }
});

carregarLista();