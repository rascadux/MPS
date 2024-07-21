import http from 'k6/http';
import { check } from 'k6';

export let options = {
  stages: [
    { duration: '1m', target: 5 }, // 5 VUs durante 1 minuto
  ],
  thresholds: {
    http_req_duration: ['p(95)<100'], // El 95% de las solicitudes deben completarse en menos de 100ms
    http_req_failed: ['rate<=0.01'], // Menos del 1% de las solicitudes deben fallar
  },
};

export default function () {
  let res = http.get('http://localhost:8080/medico/1');
  check(res, {
    'status was 200': (r) => r.status == 200,
  });
}
