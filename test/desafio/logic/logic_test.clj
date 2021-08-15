(ns desafio.logic.logic-test
  (:require [clojure.test :refer :all]
            [desafio.logic.logic :refer :all]
            [desafio.model.cliente :as d.cliente]
            [desafio.model.cartao :as d.cartao]))

(def cliente1 (d.cliente/cria-novo-cliente
                "Gabriel Lima"
                "123456789"
                "gabriel.lima@email.com"))

(def cartao1 (d.cartao/cria-novo-cartao
               2555
               155
               "07/28"
               5000M
               cliente1))

(deftest adiciona-compra-test
  (testing "Que a compra é feita com limite disponível"
    (is (= :sucesso (get (adiciona-compra (java.util.Date.)
                                             1000M
                                             "Padaria Nova"
                                             "Alimentação"
                                             cartao1) :resultado))))

  (testing "Que a conpra não é efetuada se não houver limite"
    (is (= :limite-indisponivel (get (adiciona-compra (java.util.Date.)
                                                             7000M
                                                             "Padaria Nova"
                                                             "Alimentação"
                                                      cartao1) :resultado))))
  )
