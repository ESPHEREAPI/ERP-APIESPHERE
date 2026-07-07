/**
 * Declenchement manuel des synchronisations depuis le dashboard.
 * Appelle en AJAX les endpoints REST exposes par SynchronisationController
 * (meme format de reponse legacy { status, status_message, data }).
 */

function escapeHtml(value) {
    if (value === null || value === undefined) return '';
    return String(value)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

function afficherToast(titre, message, variante) {
    const container = document.getElementById('toastContainer');
    if (!container) return;

    const couleurs = {
        success: 'text-bg-success',
        warning: 'text-bg-warning',
        danger: 'text-bg-danger',
        info: 'text-bg-primary'
    };

    const toastEl = document.createElement('div');
    toastEl.className = 'toast align-items-center border-0 ' + (couleurs[variante] || couleurs.info);
    toastEl.setAttribute('role', 'alert');
    toastEl.innerHTML =
        '<div class="d-flex">' +
        '  <div class="toast-body"><strong>' + escapeHtml(titre) + '</strong><br>' + escapeHtml(message) + '</div>' +
        '  <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>' +
        '</div>';

    container.appendChild(toastEl);
    const toast = new bootstrap.Toast(toastEl, { delay: 6000 });
    toast.show();
    toastEl.addEventListener('hidden.bs.toast', () => toastEl.remove());
}

function construireListe(lignes) {
    if (!lignes || lignes.length === 0) {
        return '<p class="text-muted small mb-0">(aucune)</p>';
    }
    const items = lignes.map(l => '<li>' + escapeHtml(l) + '</li>').join('');
    return '<ul class="result-list list-unstyled mb-0">' + items + '</ul>';
}

function afficherResultat(label, payload) {
    const card = document.getElementById('resultatCard');
    const titre = document.getElementById('resultatTitre');
    const body = document.getElementById('resultatBody');
    if (!card || !body) return;

    titre.textContent = label;

    const data = payload.data;
    const estRapport = data && typeof data === 'object' && ('nbTraites' in data);

    if (estRapport) {
        const nbTraites = data.nbTraites ?? 0;
        const nbEchecs = data.nbEchecs ?? 0;
        body.innerHTML =
            '<div class="d-flex gap-2 mb-3">' +
            '  <span class="badge bg-success-subtle text-success fs-6"><i class="bi bi-check-circle me-1"></i>' + nbTraites + ' traites</span>' +
            '  <span class="badge ' + (nbEchecs > 0 ? 'bg-danger-subtle text-danger' : 'bg-light text-muted') + ' fs-6">' +
            '    <i class="bi bi-exclamation-triangle me-1"></i>' + nbEchecs + ' echecs</span>' +
            '</div>' +
            '<div class="row g-3">' +
            '  <div class="col-md-6">' +
            '    <h3 class="h6 text-muted">Lignes traitees</h3>' +
            construireListe(data.lignesTraitees) +
            '  </div>' +
            '  <div class="col-md-6">' +
            '    <h3 class="h6 text-muted">Lignes en echec</h3>' +
            construireListe(data.lignesEnEchec) +
            '  </div>' +
            '</div>';
    } else {
        body.innerHTML = '<p class="mb-0">' + escapeHtml(payload.status_message || JSON.stringify(data)) + '</p>';
    }

    card.style.display = '';
    card.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

async function lancerTraitement(button) {
    const path = button.getAttribute('data-action-path');
    const inputId = button.getAttribute('data-action-input');
    const label = button.getAttribute('data-action-label') || path;
    const confirmMsg = button.getAttribute('data-action-confirm');

    if (confirmMsg && !window.confirm(confirmMsg)) {
        return;
    }

    let url = path;
    if (inputId) {
        const input = document.getElementById(inputId);
        const valeur = input ? input.value.trim() : '';
        if (valeur) {
            url += '/' + encodeURIComponent(valeur);
        }
    }

    const spinner = button.querySelector('.spinner-border');
    const etatInitial = button.innerHTML;
    button.disabled = true;
    if (spinner) spinner.classList.remove('d-none');

    try {
        const reponse = await fetch(url, { method: 'POST' });
        const payload = await reponse.json();

        afficherResultat(label, payload);

        if (payload.status === '200') {
            const nbEchecs = payload.data && payload.data.nbEchecs;
            if (nbEchecs && nbEchecs > 0) {
                afficherToast(label, nbEchecs + ' ligne(s) en echec - voir le detail ci-dessous.', 'warning');
            } else {
                afficherToast(label, 'Traitement termine avec succes.', 'success');
            }
        } else {
            afficherToast(label, payload.status_message || 'Le traitement a echoue.', 'danger');
        }
    } catch (erreur) {
        afficherToast(label, 'Erreur reseau : ' + erreur.message, 'danger');
    } finally {
        button.disabled = false;
        button.innerHTML = etatInitial;
    }
}
