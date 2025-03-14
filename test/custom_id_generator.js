if (!global.customerId) {
    global.customerId = 100407; // 100407부터 시작
}

function getNextCustomerId(context, events, done) {
    context.vars.customer_id = global.customerId++; // 원자적 증가
    return done();
}

module.exports = { getNextCustomerId };