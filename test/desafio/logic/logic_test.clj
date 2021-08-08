(ns desafio.logic.logic-test
  (:require [clojure.test :refer :all]
            [desafio.logic.logic :refer :all])
  (:use [java-time :only [local-date local-date?]]))

(deftest adiciona-compra-test
  (testing "Que a compra é feita com limite disponível"
    (is (= 1 (count (get (adiciona-compra [] (local-date 2021 8 3)
                                             1000
                                             "Padaria Nova"
                                             "Alimentação"
                                             2555) :compras)))))

  (testing "Que a conpra não é efetuada se não houver limite"
    (is (= :limite-indisponivel (get (adiciona-compra [] (local-date 2021 8 3)
                                                             7000
                                                             "Padaria Nova"
                                                             "Alimentação"
                                                             2555) :resultado))))
  )
