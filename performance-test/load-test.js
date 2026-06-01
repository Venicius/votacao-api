import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 10,
    duration: '10s',
};

export default function () {
    const sessaoId = "bf1dadb1-8e9d-4477-915e-dcc11852a6f7" //mudar após criar sessao
    const url = `http://localhost:8080/v1/sessoes/${sessaoId}/votos`;

    const payload = JSON.stringify({
        valor: Math.random() > 0.5 ? 'SIM' : 'NAO'
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(url, payload, params);

    check(res, {
        'Voto registado com sucesso (Status 200/201/202)': (r) => r.status === 200 || r.status === 201 || r.status === 202,
        'Tempo de resposta < 500ms': (r) => r.timings.duration < 500
    });

    sleep(0.1);
}