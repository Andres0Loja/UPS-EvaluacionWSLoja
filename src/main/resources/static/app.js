const apiBaseUrl = (window.APP_CONFIG && window.APP_CONFIG.apiBaseUrl) || "";

const selectors = {
  personaForm: document.querySelector("#persona-form"),
  personaMessage: document.querySelector("#persona-create-message"),
  buscarForm: document.querySelector("#buscar-form"),
  personaResult: document.querySelector("#persona-result"),
  tituloForm: document.querySelector("#titulo-form"),
  tituloMessage: document.querySelector("#titulo-create-message"),
  titulosForm: document.querySelector("#titulos-form"),
  titulosTable: document.querySelector("#titulos-table"),
  titulosMessage: document.querySelector("#titulos-message"),
  refreshPersonas: document.querySelector("#refresh-personas"),
  personasList: document.querySelector("#personas-list")
};

async function request(path, options = {}) {
  const response = await fetch(`${apiBaseUrl}${path}`, {
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {})
    },
    ...options
  });

  if (!response.ok) {
    let message = `Error HTTP ${response.status}`;
    try {
      const error = await response.json();
      message = formatError(error);
    } catch (_ignored) {
      message = response.statusText || message;
    }
    throw new Error(message);
  }

  if (response.status === 204) {
    return null;
  }
  return response.json();
}

function formatError(error) {
  if (error.fields && Object.keys(error.fields).length > 0) {
    return Object.values(error.fields).join(". ");
  }
  return error.message || error.error || "No se pudo completar la operacion";
}

function setMessage(element, text, type = "") {
  element.textContent = text;
  element.className = `message ${type}`.trim();
}

function formValue(id) {
  return document.querySelector(id).value.trim();
}

function renderPersona(persona) {
  return `
    <div class="result-card">
      <strong>${escapeHtml(persona.nombre)}</strong>
      <div>Cedula: ${escapeHtml(persona.cedula)}</div>
      <div>Telefono: ${escapeHtml(persona.telefono || "No registrado")}</div>
      <div>Titulos: ${persona.totalTitulos}</div>
    </div>
  `;
}

function renderPersonaCard(persona) {
  return `
    <article class="person-card">
      <strong>${escapeHtml(persona.nombre)}</strong>
      <div>${escapeHtml(persona.cedula)}</div>
      <div>${escapeHtml(persona.telefono || "Sin telefono")}</div>
      <div>${persona.totalTitulos} titulo(s)</div>
    </article>
  `;
}

function renderTitulos(titulos) {
  selectors.titulosTable.innerHTML = titulos.map((titulo) => `
    <tr>
      <td>${titulo.id}</td>
      <td>${escapeHtml(titulo.nombre)}</td>
      <td>${escapeHtml(titulo.universidad)}</td>
      <td>${escapeHtml(titulo.fechaRegistro)}</td>
    </tr>
  `).join("");
}

function escapeHtml(value) {
  return String(value)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

selectors.personaForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  setMessage(selectors.personaMessage, "");

  try {
    const persona = await request("/api/personas", {
      method: "POST",
      body: JSON.stringify({
        cedula: formValue("#persona-cedula"),
        nombre: formValue("#persona-nombre"),
        telefono: formValue("#persona-telefono")
      })
    });
    selectors.personaForm.reset();
    setMessage(selectors.personaMessage, `Persona creada: ${persona.nombre}`, "success");
    await loadPersonas();
  } catch (error) {
    setMessage(selectors.personaMessage, error.message, "error");
  }
});

selectors.buscarForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  selectors.personaResult.innerHTML = "";

  try {
    const cedula = formValue("#buscar-cedula");
    const persona = await request(`/api/personas/${encodeURIComponent(cedula)}`);
    selectors.personaResult.innerHTML = renderPersona(persona);
  } catch (error) {
    selectors.personaResult.innerHTML = `<div class="message error">${escapeHtml(error.message)}</div>`;
  }
});

selectors.tituloForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  setMessage(selectors.tituloMessage, "");

  try {
    const cedula = formValue("#titulo-cedula");
    const titulo = await request(`/api/personas/${encodeURIComponent(cedula)}/titulos`, {
      method: "POST",
      body: JSON.stringify({
        nombre: formValue("#titulo-nombre"),
        universidad: formValue("#titulo-universidad")
      })
    });
    selectors.tituloForm.reset();
    setMessage(selectors.tituloMessage, `Titulo registrado: ${titulo.nombre}`, "success");
    document.querySelector("#titulos-cedula").value = cedula;
    await loadTitulos(cedula);
    await loadPersonas();
  } catch (error) {
    setMessage(selectors.tituloMessage, error.message, "error");
  }
});

selectors.titulosForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  await loadTitulos(formValue("#titulos-cedula"));
});

selectors.refreshPersonas.addEventListener("click", loadPersonas);

async function loadTitulos(cedula) {
  setMessage(selectors.titulosMessage, "");
  selectors.titulosTable.innerHTML = "";

  try {
    const titulos = await request(`/api/personas/${encodeURIComponent(cedula)}/titulos`);
    renderTitulos(titulos);
    setMessage(selectors.titulosMessage, `${titulos.length} titulo(s) encontrado(s)`, "success");
  } catch (error) {
    setMessage(selectors.titulosMessage, error.message, "error");
  }
}

async function loadPersonas() {
  try {
    const personas = await request("/api/personas");
    selectors.personasList.innerHTML = personas.length
      ? personas.map(renderPersonaCard).join("")
      : '<div class="message">No hay personas registradas.</div>';
  } catch (error) {
    selectors.personasList.innerHTML = `<div class="message error">${escapeHtml(error.message)}</div>`;
  }
}

loadPersonas();
